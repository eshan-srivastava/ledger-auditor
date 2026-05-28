package internal


// The main struct parsing seed-config.yaml
type SeedConfig struct {
	Profiles   []ProfileEntry `yaml:"profiles"`
	Base       BaseSettings   `yaml:"base"`
	Categories []string       `yaml:"categories"`
}

type direction string

const (
	Credited direction = "credit"
	Debited  direction = "debit"
)

type DateRange struct {
	Start string `yaml:"start"`
	End   string `yaml:"end"`
}

type BaseSettings struct {
	Seed              int       `yaml:"seed"`
	DateRange         DateRange `yaml:"date_range"`
	Currency          string    `yaml:"currency"`
	CleanDBIfNotEmpty bool      `yaml:"clean_db_if_nonempty"`
	WriteToDB         bool      `yaml:"write_to_db"`
}

type ProfileEntry struct {
	Name     string         `yaml:"name"`
	Count    int32          `yaml:"count"`
	Accounts []AccountEntry `yaml:"accounts"`
	Rules    []RuleEntry    `yaml:"rules"`
}

type AccountEntry struct {
	Id             string   `yaml:"id"`
	Type           string   `yaml:"type"`
	OpeningBalance int64    `yaml:"opening_balance"` // was: openeing_balance (typo)
	CreditLimit    *int64   `yaml:"credit_limit,omitempty"`
	Bnpl           *BNPLCfg `yaml:"bnpl,omitempty"`
}

type MinMaxRange struct {
	Min int64 `yaml:"min"`
	Max int64 `yaml:"max"`
}

// RuleAmount covers all four amount modes in the config.
//   - Range:         uniform random between [Min, Max]
//   - Fixed:         exact value
//   - Pct:           pct_of_source — percentage of the triggering transaction
//   - SettleAccount: compute outstanding balance of the named account at fire time
type RuleAmount struct {
	Range         *MinMaxRange `yaml:"range,omitempty"`
	Fixed         *int64       `yaml:"fixed,omitempty"`
	Pct           *float64     `yaml:"pct_of_source,omitempty"`
	SettleAccount *string      `yaml:"settle_account,omitempty"`
}

type BNPLCfg struct {
	InstallmentCount int `yaml:"installment_count"`
	InstallmentDay   int `yaml:"installment_day"`
}

// RuleFrequency controls random_n_per_month and decay_pattern transaction counts.
type RuleFrequency struct {
	Mean     int `yaml:"mean"`
	Variance int `yaml:"variance"`
}

// DecayCfg models the decay_pattern rule: high frequency early in the month,
// falling to trough_frequency after inflection_day.
type DecayCfg struct {
	PeakFrequency   int `yaml:"peak_frequency"`
	TroughFrequency int `yaml:"trough_frequency"`
	InflectionDay   int `yaml:"inflection_day"`
}

// RefundDelay holds the delay-day range for refund_shadow rules.
type RefundDelay struct {
	Range MinMaxRange `yaml:"range"`
}

type RuleEntry struct {
	// Common fields
	Type        string    `yaml:"type"`
	Day         int32     `yaml:"day,omitempty"`
	Account     string    `yaml:"account,omitempty"`
	Direction   direction `yaml:"direction,omitempty"`
	Category    string    `yaml:"category,omitempty"`
	Description string    `yaml:"description,omitempty"`
	Amount      RuleAmount `yaml:"amount,omitempty"`

	// monthly_day_range / random_n_per_month / one_time_in_range
	DayRange *MinMaxRange `yaml:"day_range,omitempty"`

	// weekly
	Weekday string `yaml:"weekday,omitempty"`

	// reactive_pct
	TriggerAccount  string `yaml:"trigger_account,omitempty"`
	TriggerCategory string `yaml:"trigger_category,omitempty"`
	FromAccount     string `yaml:"from_account,omitempty"`
	ToAccount       string `yaml:"to_account,omitempty"`

	// random_n_per_month / decay_pattern
	Frequency *RuleFrequency `yaml:"frequency,omitempty"`

	// decay_pattern
	Decay *DecayCfg `yaml:"decay,omitempty"`

	// one_time_in_range
	Probability *float64 `yaml:"probability,omitempty"`

	// refund_shadow
	SourceCategory    string       `yaml:"source_category,omitempty"`
	SourceAccount     string       `yaml:"source_account,omitempty"`
	RefundProbability *float64     `yaml:"refund_probability,omitempty"`
	RefundDelayDays   *RefundDelay `yaml:"refund_delay_days,omitempty"`
}
