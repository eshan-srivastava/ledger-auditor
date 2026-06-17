"""
Choose model based on selection
"""

from app.core.config import AppSettings
from app.infra.llm.base import LLMClient
from app.infra.llm.gemini import GeminiLLMClient


def get_llm_client(provider: str | None = None) -> LLMClient:
    """
    Factory method that returns a llm client that is implementation of base LLMClient ABC.
    Change provider str OR Appsettings.llm_provider to change between the model that we are using
    """
    target_provider = provider or AppSettings.llm_provider

    if target_provider.lower == "gemini":
        return GeminiLLMClient()
    
    raise ValueError(f"Unsupported LLM Provider called: {target_provider}")