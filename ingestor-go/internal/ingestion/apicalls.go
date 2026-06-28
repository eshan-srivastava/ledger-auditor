// Ingestion via API calls + polling

package ingestion

import (
	"context"
	"fmt"
	"os"
	"time"

	"github.com/eshan-srivastava/ingestor-go/internal/lib"
	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

var (
	BASE_URL        = os.Getenv("SPRING_API_BASE_URL")
	GETTxnListRoute = "transactions/"
)

func GetUpstreamTransactions(
	ctx context.Context,
	lastSeenTimestamp time.Time,
	lastId string,
	limit int,
	hc *lib.HTTPClient,
) (*models.ExtTransactionListResp, error) {
	// construct query params
	queryUrl := fmt.Sprintf("fromDate=%s&afterId=%s&size=%d", lastSeenTimestamp.Format(time.DateOnly), lastId, limit)

	finalUrl := fmt.Sprintf("%s/%s?%s", BASE_URL, GETTxnListRoute, queryUrl)

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
