package internal

import "time"

type CreateTxnRequest struct {
	SourceAccNum      string `json:"sourceAccountNum"`
	DestinationAccNum string `json:"destinationAccountNum"`
	OriginId          int64  `json:"originId"`
	Amount            int32  `json:"amount"`
	Note              string `json:"note"`
}

type CreateAccountRequest struct {
	AccountNumber string      `json:"accountNumber"`
	AccountType   AccountType `json:"accountType"`
	UserID        int64       `json:"userId"`
}

type CreateUserRequest struct {
	Name     string `json:"name"`
	Email    string `json:"email"`
	Password string `json:"password"`
}

type CreateTxnResponse struct {
	TransactionId int64     `json:"id"`
	CreatedAt     time.Time `json:"createdAt"`
}

type CreateUserResponse struct {
	UserId    int64     `json:"userId"`
	CreatedAt time.Time `json:"createdAt"`
}

type CreateAccountResponse struct {
	AccountId int64     `json:"accountId"`
	CreatedAt time.Time `json:"createdAt"`
}

type AccountType string

const (
	Savings    AccountType = "SAVINGS"
	Current    AccountType = "CURRENT"
	Credit     AccountType = "CREDIT"
	Investment AccountType = "INVESTMENT"
	Checking   AccountType = "CHECKING"
	Trading    AccountType = "TRADING"
)
