from fastapi import APIRouter

router = APIRouter(
    prefix="/health",
    responses={404: {"description": "Not Found"}}
)

@router.get("/")
async def healthroute():
    return {"message": "server up"}