package main

import (
	"github.com/eshan-srivastava/ingestor-go/internal/api"
	"github.com/eshan-srivastava/ingestor-go/internal/db"
	"github.com/eshan-srivastava/ingestor-go/internal/services"
	"github.com/eshan-srivastava/ingestor-go/internal/telemetry"
)

func main() {
	err := telemetry.InitGlobalLogger()
	if err != nil {
		panic("error initializing logger")
	}

	zlog := telemetry.GetGlobalLogger()

	sqliteURI := ""
	sqldb, err := db.NewDB(sqliteURI)
	if err != nil {
		panic("error init sqlite db: " + err.Error())
	}

	mirrorService := services.NewMirrorService(sqldb)

	httpHandler := api.NewHttpHandler(mirrorService, zlog)

	go mirrorService.StartIngestor()
	defer mirrorService.Close()

	setupRoutes(httpHandler)
}
