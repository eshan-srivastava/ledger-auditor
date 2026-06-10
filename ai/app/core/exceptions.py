class LLMServiceException(Exception):
    """
    Base exception class for LLM service-related errors.

    This exception should be used as a base class for more specific
    LLM service exceptions.

    Attributes:
        message (str): The error message.
        status_code (int): The HTTP status code associated with this error.
    """
    def __init__(self, message: str, status_code: int):
        self.message = message
        self.status_code = status_code
        super().__init__(self.message)
        
class ModelNotFoundException(LLMServiceException):
    def __init__(self, model: str):
        message = f"Model named {model} not found"
        super().__init__(message, 404)
    
class LLMProviderError(LLMServiceException):
    def __init__(self, provider: str, details: str):
        message = f"Error with LLM provider {provider}: {details}"
        super().__init__(message=message, status_code=502)
