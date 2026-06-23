package lib

import (
	"encoding/json"
	"log"
	"net/http"
)

func JsonError(w http.ResponseWriter, code int, message string) {
	JsonWriter(w, code, map[string]any{
		"error": message,
	})
}

func JsonWriter(w http.ResponseWriter, code int, data any) {
	w.Header().Set("Content-Type", "application/json")
	err := json.NewEncoder(w).Encode(data)
	if err != nil {
		log.Printf("error encoding data jsonerr: %v\n", err.Error())
	}
}
