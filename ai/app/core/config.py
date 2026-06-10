from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import Field

class AppSettings(BaseSettings):
    """
    Application configuration class.

    This class uses Pydantic's BaseSettings to manage configuration variables,
    allowing for easy loading from environment variables and .env files.

    Attributes:
        APP_NAME (str): The name of the application.
        DEBUG (bool): Debug mode flag.
        OPENAI_API_KEY (str): API key for OpenAI services.
        ANTHROPIC_API_KEY (str): API key for Anthropic services.
        REDIS_HOST (str): Hostname for the Redis server.
        REDIS_PORT (int): Port number for the Redis server.
        REDIS_DB (int): Redis database number to use.
        DEFAULT_MODEL (str): Default LLM model to use.
        MAX_TOKENS (int): Maximum number of tokens for LLM responses.
        LOG_LEVEL (str): Logging level for the application.
        JAEGER_HOST (str): Hostname for the Jaeger tracing server.
        JAEGER_PORT (int): Port number for the Jaeger tracing server.
    """

    app_name: str = "LEDGER-AI"

    #LLM Config
    llm_provider: str = Field("gemini")
    llm_model: str = Field("gemini-3.5-flash")
    llm_api_key: str = Field("")
    max_tokens = Field(5000)
    
    #DB Config
    postgres_dsn: str = Field("postgres://user:pass@localhost:5432" )

    #API Config
    go_base_url: str = Field("http://localhost:8088/")
    
    #Log Config
    log_level: str = Field("INFO")
    jaeger_host: str = Field("localhost")
    jaeger_port: str = Field("6831")

    model_config = SettingsConfigDict(
        env_file = ".env",
        env_file_encoding = "utf-8",
        case_sensitive=False,
        extra="ignore"
    )

# global singleton
settings = AppSettings()