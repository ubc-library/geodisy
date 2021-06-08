import os
import json
from pathlib import Path


rootDir = '.'


def update_gbl(dir):
    for root, dirs, files in os.walk(dir):
        folder_count = len(dirs);
        if folder_count > 0:
            for folder in dirs:
                fix_gbl(os.path.join(root, folder))


def get_label(folder):
    gbl = os.path.join(folder, "geoblacklight.json")
    with open(gbl) as f:
        data = json.load(f)
        return data["layer_id_s"]


def fix_gbl(folder):
    for root in os.walk(folder):
        gbl = os.path.join(root[0], "geoblacklight.json")
        with open(gbl) as f:
            data = json.load(f)
            layer = data["layer_id_s"]
            if (layer.startswith("v000") or layer.startswith("r000")) and len(layer)>11:
                new_layer = layer[0: 11]
                data["layer_id_s"] = new_layer
                data["layer_slug_s"] = "geodisy:"+new_layer
            sources = data["dc_source_sm"]
            new_sources = []
            for s in sources:
                if (s.startswith("geodisy:v000") or s.startswith("geodisy:r000")) and len(s)>19:
                    new_sources.append(s[0: 19])
                else:
                    new_sources.append(s)
            data["dc_source_sm"] = new_sources
            f.close()
        with open(gbl, "w") as outfile:
            json.dump(data, outfile)


def find_xml(rootDir):
    for root, dirs, files in os.walk(rootDir):
        base = False
        for file in files:
            if file == "iso19139.xml":
                update_gbl(root)
                base = True
        if not base:
            for folder in dirs:
                path = os.path.join(root, folder)
                if os.path.isdir(path) and not folder == ".idea":
                    find_xml(path)


if __name__ == '__main__':
    find_xml(os.getcwd())
