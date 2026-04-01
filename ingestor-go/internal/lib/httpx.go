package lib

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

type HTTPClient struct {
	hc *http.Client
}

func NewHTTPClient() *HTTPClient {
	return &HTTPClient{
		hc: &http.Client{
			Timeout: 75 * time.Second,
		},
	}
}

func (h *HTTPClient) Get(ctx context.Context, url string, outptr any) error {
	req, err := http.NewRequestWithContext(ctx, http.MethodGet, url, nil)
	if err != nil {
		return fmt.Errorf("error initing request: %w", err)
	}
	resp, err := h.hc.Do(req)
	if err != nil {
		return fmt.Errorf("error performing request: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		var errmsg string
		_, err := resp.Body.Read([]byte(errmsg))
		if err != nil {
			return fmt.Errorf("api responded non-OK %d: with unknown err resp: %w", resp.StatusCode, err)
		}
		return fmt.Errorf("api responded non-OK %d: %s", resp.StatusCode, errmsg)
	}

	if outptr == nil {
		return nil
	}
	err = json.NewDecoder(resp.Body).Decode(outptr)
	if err != nil {
		return fmt.Errorf("error decoding api response: %w", err)
	}

	return nil
}

func (h *HTTPClient) Post(ctx context.Context, url string, rawbody any) error {
	return h.Do(ctx, url, http.MethodPost, rawbody, nil)
}

func (h *HTTPClient) Do(ctx context.Context, url, method string, rawbody, outptr any) error {
	var payload []byte
	if rawbody != nil {
		p, err := json.Marshal(rawbody)
		if err != nil {
			return fmt.Errorf("error encoding request body: %w", err)
		}
		payload = p
	}

	req, err := http.NewRequestWithContext(ctx, method, url, bytes.NewBuffer(payload))
	if err != nil {
		return fmt.Errorf("error initing request: %w", err)
	}

	resp, err := h.hc.Do(req)
	if err != nil {
		return fmt.Errorf("error performing request: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode > 299 || resp.StatusCode < 200 {
		var errmsg string
		_, err := resp.Body.Read([]byte(errmsg))
		if err != nil {
			return fmt.Errorf("api responded non-OK %d: with unknown err resp: %w", resp.StatusCode, err)
		}
		return fmt.Errorf("api responded non-OK %d: %s", resp.StatusCode, errmsg)
	}
	if outptr == nil {
		return nil
	}
	err = json.NewDecoder(resp.Body).Decode(outptr)
	if err != nil {
		return fmt.Errorf("error decoding api response: %w", err)
	}

	return nil
}
