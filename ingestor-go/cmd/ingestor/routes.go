package main

import (
	"github.com/eshan-srivastava/ingestor-go/internal/api"
	"github.com/eshan-srivastava/ingestor-go/internal/telemetry"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
)

func setupRoutes(handlers *api.HttpHandler) *chi.Mux {
	r := chi.NewRouter()

	r.Use(telemetry.ZapLogger())
	r.Use(middleware.Recoverer)

	r.Route("/api/v1", func(r chi.Router) {
		r.Get("/analytics/spend", handlers.GetSpendsHandler)
		r.Get("/raw/transactions", handlers.GetTransactionsListHandler)
	})
	return r
}
