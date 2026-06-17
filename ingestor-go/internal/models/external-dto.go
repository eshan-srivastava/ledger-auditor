package models

import "time"

type UserDetailsResponse struct {
	UserID    int64     `json:"id"`
	Name      string    `json:"name"`
	Email     string    `json:"email"`
	CreatedAt time.Time `json:"createdAt"`
}

type UserBalanceResponse struct {
	UserID   int64   `json:"userId"`
	UserName string  `json:"userName"`
	Balance  float64 `json:"balance"`
}

type AccountBalanceResponse struct {
	AccountID     int64   `json:"accountId"`
	AccountNumber string  `json:"accountNumer"`
	Balance       float64 `json:"balance"`
}

type TransactionByIDResponse struct {
	TxnID     int64     `json:"id"`
	Amount    float64   `json:"amount"`
	Timestamp time.Time `json:"timestamp"`
	// OriginID float64
	SourceAccNum      string `json:"sourceAccountNum"`
	DestinationAccNum string `json:"destinationAccountNum"`
}

type TransactionListRespElem = TransactionByIDResponse

type TransactionListResponse struct {
	Content     []TransactionListRespElem `json:"content"`
	PageDetails PageableDetails           `json:"pageable"`

	// Top Level Pageable
	Total      int  `json:"total"`
	TotalPages int  `json:"totalPages"`
	Number     int  `json:"number"`
	Size       int  `json:"size"`
	First      bool `json:"first"`
	Last       bool `json:"last"`
	Empty      bool `json:"empty"`
}

type PageableDetails struct {
	PageNumber int  `json:"pageNumber"`
	PageSize   int  `json:"pageSize"`
	Offset     int  `json:"offset"`
	Paged      bool `json:"paged"`
	Unpaged    bool `json:"unpaged"`
}
