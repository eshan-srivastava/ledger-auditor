package db

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"log"

	"github.com/eshan-srivastava/ingestor-go/internal/models"
	"github.com/jmoiron/sqlx"
	_ "modernc.org/sqlite"
)

type SqLDB struct {
	db *sqlx.DB
}

func NewDB(dbURI string) (*SqLDB, error) {
	db, err := sqlx.Connect("sqlite", dbURI)

	if err != nil {
		return nil, err
	}
	return &SqLDB{
		db: db,
	}, nil
}

func (d *SqLDB) EnsureTableAndIndex() error {

	_, err := d.db.ExecContext(
		context.Background(),
		`CREATE TABLE IF NOT EXISTS transactions(
			id INTEGER PRIMARY KEY AUTOINCREMENT,
			amount REAL NOT NULL,
			created_at REAL NOT NULL,
			source_acc_id INTEGER NOT NULL,
			dest_acc_id INTEGER NOT NULL,
			note_string TEXT NOT NULL,
			checksum TEXT NOT NULL,
		);
		CREATE INDEX idx_created_at ON transactions(created_at);
		CREATE INDEX idx_source_acc ON transactions(source_acc_id);
		CREATE INDEX idx_dest_acc ON transactions(dest_acc_ind);	
		`,
	)
	return err
}

func (d *SqLDB) Close() {
	err := d.db.Close()
	if err != nil {
		log.Printf("error closing db: %v", err)
	}
}

func (d *SqLDB) InsertTransaction(ctx context.Context, rt models.ReadTransaction) error {
	result, err := d.db.NamedExecContext(ctx, create_txn, &rt)
	if err != nil {
		return fmt.Errorf("error inserting transaction: %w", err)
	}

	affected, err := result.RowsAffected()
	if err != nil || affected == 0 {
		return fmt.Errorf("no rows affected (err): %w", err)
	}

	return nil
}

func (d *SqLDB) FetchTransactionByFilters(ctx context.Context, filters map[string]any) (*models.PagedTxnListResponse, error) {

	return nil, nil
}

func (d *SqLDB) FetchTransactionById(ctx context.Context, txnID float64) (*models.TxnResponse, error) {
	var rt models.ReadTransaction
	err := d.db.GetContext(ctx, &rt, select_1_txn, txnID)

	if errors.Is(err, sql.ErrNoRows) {
		return nil, models.ErrTxnNotFound
	}
	if err != nil {
		return nil, fmt.Errorf("error getting transaction: %w", err)
	}

	return &rt, nil
}

func (d *SqLDB) FetchTransactionList(ctx context.Context, txnID []float64) ([]models.TxnResponse, error)
