from typing import List, Dict
from pydantic import BaseModel

class Item(BaseModel):
    qty: int
    menu_id: int
    price: int


class SaleItem(BaseModel):
    data: List[Dict[str, int]] = []