package models

// TODO: populate these 2

type SpringSourceTxn struct{}

type SpendResponse struct{}

type TxnResponse = ReadTransaction

type PagedTxnListResponse struct {
	Transactions []ReadTransaction `json:"transactions"`
	Total        int               `json:"total"`
	NextId       int64             `json:"next_id"`
}

type TxnListResponse struct {
	Transactions []ReadTransaction `json:"transactions"`
}
