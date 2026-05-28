package internal

import (
	"bytes"
	"encoding/json"
	"io"
	"net/http"
	"strings"
	"testing"
	"time"
)

type mockRoundTripper struct {
	roundTripFunc func(req *http.Request) (*http.Response, error)
}

func (m *mockRoundTripper) RoundTrip(req *http.Request) (*http.Response, error) {
	return m.roundTripFunc(req)
}

func TestSeedConfigParsingAndDryRun(t *testing.T) {
	// 1. Read seed-config.yaml
	cfg, err := readSeedConfig("../seed-config.yaml")
	if err != nil {
		t.Fatalf("Failed to read seed config: %v", err)
	}

	if len(cfg.Profiles) == 0 {
		t.Errorf("Expected profiles, got 0")
	}
	if len(cfg.Categories) == 0 {
		t.Errorf("Expected categories, got 0")
	}

	// 2. Setup mock HttpClient
	httpClient := GetHttpClient()
	var userCount, accCount, txnCount int

	httpClient.hc.Transport = &mockRoundTripper{
		roundTripFunc: func(req *http.Request) (*http.Response, error) {
			var body []byte
			var err error

			if strings.Contains(req.URL.Path, BaseUsers) {
				userCount++
				resp := CreateUserResponse{
					UserId:    int64(userCount),
					CreatedAt: time.Now(),
				}
				body, err = json.Marshal(resp)
			} else if strings.Contains(req.URL.Path, BaseAccounts) {
				accCount++
				resp := CreateAccountResponse{
					AccountId: int64(accCount),
					CreatedAt: time.Now(),
				}
				body, err = json.Marshal(resp)
			} else if strings.Contains(req.URL.Path, BaseTxn) {
				txnCount++
				resp := CreateTxnResponse{
					TransactionId: int64(txnCount),
					CreatedAt:     time.Now(),
				}
				body, err = json.Marshal(resp)
			}

			if err != nil {
				return nil, err
			}

			return &http.Response{
				StatusCode: http.StatusOK,
				Body:       io.NopCloser(bytes.NewBuffer(body)),
				Header:     make(http.Header),
			}, nil
		},
	}

	// 3. Run the generator dry-run
	cfg.Base.CleanDBIfNotEmpty = false
	pg := &PGDB{}
	generator := getGenerator(httpClient, cfg, pg)
	err = generator.startSeeding(cfg)
	if err != nil {
		t.Fatalf("Seeding failed: %v", err)
	}

	t.Logf("Successfully seeded: %d users, %d accounts, %d transactions", userCount, accCount, txnCount)
}
