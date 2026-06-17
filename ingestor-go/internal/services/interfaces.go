package services

import (
	"context"

	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

type TransactionRepository interface {
	InsertTransaction(ctx context.Context, rt models.ReadTransaction) error
	FetchTransactionByFilters(ctx context.Context, filters map[string]any) (*models.PagedTxnListResponse, error)
	FetchTransactionById(ctx context.Context, txnID float64) (*models.TxnResponse, error)
}
