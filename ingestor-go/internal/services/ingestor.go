package services

import (
	"context"
	"fmt"
	"strconv"

	"github.com/eshan-srivastava/ingestor-go/internal/lib"
	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

type MirrorService struct {
	txnRepo TransactionRepository
	httpC   *lib.HTTPClient // replace with an interface

}

func NewMirrorService(
	trepo TransactionRepository,
) *MirrorService {
	hc := lib.NewHTTPClient()
	return &MirrorService{
		txnRepo: trepo,
		httpC:   hc,
	}
}

func (s *MirrorService) Close() {
}

func (s *MirrorService) AddTransaction() error {
	return nil
}

func (s *MirrorService) GetTransactionsWithFilters() {

}

func (s *MirrorService) GetTransactionsByIDList(ctx context.Context, rawIdList []string) (*models.TxnListResponse, error) {
	//convert string to int64

	intIds := make([]int64, len(rawIdList))
	for i, idStr := range rawIdList {
		id, err := strconv.Atoi(idStr)
		if err != nil {
			return nil, fmt.Errorf("invalid transactionId (%s): %w", idStr, err)
		}
		intIds[i] = int64(id)
	}

	txns, err := s.txnRepo.FetchTransactionList(ctx, intIds)
	if err != nil {
		return nil, fmt.Errorf("error fetching transactions: %w", err)
	}

	return &models.TxnListResponse{
		Transactions: txns,
	}, nil
}

func (s *MirrorService) StartIngestor() {
	// should call the worker pool and use it in open fashion to process ingestion
}
