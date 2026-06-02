package main

import (
	"fmt"
	"os"

	"dataseeder/internal"
)

func main() {
	if os.Getenv("RUN_SEEDER") != "true" {
		fmt.Println("RUN_SEEDER is not set to true; seeder is disabled.")
		os.Exit(0)
	}

	internal.SeedData()
}
