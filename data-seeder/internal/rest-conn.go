package internal

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

const (
	BASE_API     = "http://localhost/api"
	BaseUsers    = "/users"
	BaseTxn      = "/transactions"
	BaseAccounts = "/accounts"
)

type HTTPClient struct {
	hc http.Client
}

func GetHttpClient() *HTTPClient {
	return &HTTPClient{
		hc: http.Client{
			Timeout: 10 * time.Second,
		},
	}
}

func (h *HTTPClient) CreateUser(inp *CreateUserRequest) (*CreateUserResponse, error) {
	var out CreateUserResponse
	err := h.doPost(BASE_API+BaseUsers, inp, &out)
	return &out, err
}

func (h *HTTPClient) CreateAccount(inp *CreateAccountRequest) (*CreateAccountResponse, error) {
	var out CreateAccountResponse
	err := h.doPost(BASE_API+BaseAccounts, inp, &out)
	return &out, err
}

func (h *HTTPClient) CreateTransaction(inp *CreateTxnRequest) (*CreateTxnResponse, error) {
	var out CreateTxnResponse
	err := h.doPost(BASE_API+BaseTxn, inp, &out)
	return &out, err
}

func (h *HTTPClient) doPost(req string, rawbody any, outptr any) error {
	jsonbody, err := json.Marshal(rawbody)
	if err != nil {
		return err
	}

	resp, err := h.hc.Post(req, "application/json", bytes.NewBuffer(jsonbody))
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode > 299 {
		return fmt.Errorf("server replied with non 200: %d", resp.StatusCode)
	}

	err = json.NewDecoder(resp.Body).Decode(outptr)
	if err != nil {
		return err
	}

	return nil
}
