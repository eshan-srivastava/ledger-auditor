"""
Core Module

This module contains core components of the LLM later,
including configuration init and custom exceptions.

Components:
- config: Application configuration management
- exceptions: Custom exception classes for the application

Usage:
    from core.config import settings
    from core.exceptions import LLMServiceException

    # Access configuration
    api_key = settings.LLM_API_KEY

    # Raise a custom exception
    raise LLMServiceException("An error occurred in the LLM service")
"""

from .config import AppSettings
from .exceptions import LLMServiceException

__all__ = ["settings", "LLMServiceException"]

# Version of core module ? idk why we add this yet
__version__ = "0.1.0"