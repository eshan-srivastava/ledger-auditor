package db

import (
	"context"
	"database/sql"
	"errors"
	"fmt"

	"github.com/eshan-srivastava/ingestor-go/internal/models"
)

func (d *SqLDB) GetPollCursor(ctx context.Context) (*models.PollerCursor, error) {
	var pc models.PollerCursor
	err := d.db.GetContext(ctx, &pc, select_cursor)
	if errors.Is(err, sql.ErrNoRows) {
		return nil, models.ErrEmptyPollerCursor
	}
	if err != nil {
		return nil, fmt.Errorf("error getting poller cursor: %w", err)
	}

	return &pc, nil
}

func (d *SqLDB) SetPollCursor(ctx context.Context, pc *models.PollerCursor) error {
	return nil
}
