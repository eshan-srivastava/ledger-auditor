package db

import (
	"context"
	"database/sql"
	"log"

	"github.com/eshan-srivastava/ingestor-go/internal/models"
	_ "modernc.org/sqlite"
)

type SqLDB struct {
	db *sql.DB
}

func GetDB(dbURI string) (*SqLDB, error) {
	db, err := sql.Open("sqlite", dbURI)

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

func (d *SqLDB) addTransaction() error {

}

func (d *SqLDB) fetchTransactionByFilters() ([]models.ReadTransaction, error) {

}
