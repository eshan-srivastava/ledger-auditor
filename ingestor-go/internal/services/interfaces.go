package services

import (
	"context"

	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

// Accessing transaction data from sqlite copy
type TransactionRepository interface {
	InsertTransaction(ctx context.Context, rt models.ReadTransaction) error
	FetchTransactionByFilters(ctx context.Context, filters map[string]any) (*models.PagedTxnListResponse, error)
	FetchTransactionById(ctx context.Context, txnID int64) (*models.TxnResponse, error)
	FetchTransactionList(ctx context.Context, txnIDs []int64) ([]models.TxnResponse, error)
}

// Accessing transaction data from origin
type DataOrigin interface {
}
