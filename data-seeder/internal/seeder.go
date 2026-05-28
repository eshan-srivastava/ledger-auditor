package internal

import (
	"fmt"
	"os"

	"go.yaml.in/yaml/v3"
)

func SeedData() {
	fmt.Println("Starting data seeding process...")

	cfg, err := readSeedConfig("seed-config.yaml")
	if err != nil {
		fmt.Printf("Error: failed to read seed-config.yaml: %v\n", err)
		os.Exit(1)
	}

	httpClient := GetHttpClient()
	pgdb := GetPostgres()
	generator := getGenerator(httpClient, cfg, pgdb)

	fmt.Printf("Seeding profiles over date range %s to %s...\n", cfg.Base.DateRange.Start, cfg.Base.DateRange.End)
	if err := generator.startSeeding(cfg); err != nil {
		fmt.Printf("Error: seeding failed: %v\n", err)
		os.Exit(1)
	}

	fmt.Println("Data seeding completed successfully!")
}

func readSeedConfig(cfgpath string) (*SeedConfig, error) {
	f, err := os.Open(cfgpath)
	if err != nil {
		return nil, fmt.Errorf("error reading seedconfig: %w", err)
	}

	decoder := yaml.NewDecoder(f)

	var cfg SeedConfig
	if err := decoder.Decode(&cfg); err != nil {
		return nil, fmt.Errorf("error reading into seedconfg: %w", err)
	}

	return &cfg, nil
}
