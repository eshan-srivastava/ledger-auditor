package main

import "github.com/eshan-srivastava/ingestor-go/internal/telemetry"

func main() {
	err := telemetry.InitGlobalLogger()
	if err != nil {
		panic("error initializing logger")
	}
}
