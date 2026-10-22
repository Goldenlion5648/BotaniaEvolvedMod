import json
from pathlib import Path

FLOWERS = [
    "dandelifeon",
    "endoflame",
    "entropinnyum",
    "gourmaryllis",
    "hydroangeas",
    "kekimurus",
    "munchdew",
    "narslimmus",
    "rafflowsia",
    "arcanerose",
    "shulk_me_not",
    "spectrolus",
    "thermalily",
]

# OUTPUT = Path(__file__+ "/..") 

for flower in FLOWERS:
    for prefix in ("", "floating_"):
        name = prefix + flower

        loot_table = {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [
                        {
                            "type": "minecraft:item",
                            "name": f"botania:{name}",
                            "functions": [
                                {
                                    "function": "minecraft:copy_nbt",
                                    "source": "block_entity",
                                    "ops": [
                                        {
                                            "source": "ticksExisted",
                                            "target": "BlockEntityTag.ticksExisted",
                                            "op": "replace",
                                        }
                                    ],
                                }
                            ],
                        }
                    ],
                    "conditions": [
                        {
                            "condition": "minecraft:survives_explosion"
                        }
                    ],
                }
            ],
        }

        path = Path(f"{name}.json")
#         path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(json.dumps(loot_table, indent=2) + "\n")

        print(f"Generated {path}")