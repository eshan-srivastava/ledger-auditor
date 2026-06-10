package models

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"time"
)

type ReadTransaction struct {
	TransactionID int64     `json:"id"`
	Amount        float64   `json:"amount"`
	CreatedAt     time.Time `json:"createdAt"`
	SourceAccID   int64     `json:"sourceAccId"`
	DestAccID     int64     `json:"destAccId"`
	NoteString    string    `json:"noteString"`
	Checksum      string
}

func (rt *ReadTransaction) ValidateChecksum() error {
	if rt.Checksum == "" {
		return ErrCheckSumEmpty
	}
	inpStr := fmt.Sprintf("%d:%f:%s:%d:%d", rt.TransactionID, rt.Amount, rt.CreatedAt, rt.SourceAccID, rt.DestAccID)

	rawhash := sha256.Sum256([]byte(inpStr))
	newChecksum := hex.EncodeToString(rawhash[:])

	if rt.Checksum == string(newChecksum) {
		return nil
	}
	return ErrChecksumMismatch
}

func (rt *ReadTransaction) ComputeChecksum() {
	inpStr := fmt.Sprintf("%d:%f:%s:%d:%d", rt.TransactionID, rt.Amount, rt.CreatedAt, rt.SourceAccID, rt.DestAccID)

	rawhash := sha256.Sum256([]byte(inpStr))
	newChecksum := hex.EncodeToString(rawhash[:])
	rt.Checksum = newChecksum
}
