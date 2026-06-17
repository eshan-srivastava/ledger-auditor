package services

import (
	"github.com/eshan-srivastava/ingestor-go/internal/lib"
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

func (s *MirrorService) GetTransactionsByIDList() {}

func (s *MirrorService) StartIngestor() {
	// should call the worker pool and use it in open fashion to process ingestion
}
