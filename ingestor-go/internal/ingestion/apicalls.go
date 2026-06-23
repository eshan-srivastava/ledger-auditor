// Ingestion via API calls + polling

package ingestion

import (
	"context"
	"fmt"
	"os"

	"github.com/eshan-srivastava/ingestor-go/internal/lib"
	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

var (
	BASE_URL        = os.Getenv("SPRING_API_BASE_URL")
	GETTxnListRoute = "transactions/"
)

func GetUpstreamTransactions(ctx context.Context, hc *lib.HTTPClient) (*models.ExtTransactionListResp, error) {
	finalUrl := fmt.Sprintf("%s/%s", BASE_URL, GETTxnListRoute)

	// construct query params

	if hc == nil {
		return nil, fmt.Errorf("httpClient nil")
	}

	var resp models.ExtTransactionListResp
	err := hc.Get(ctx, finalUrl, &resp)
	if err != nil {
		return nil, fmt.Errorf("transactionlist API error: %w", err)
	}

	return &resp, nil
}
