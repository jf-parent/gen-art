#!/usr/bin/env bash

for i in *; do
    uuid=$(uuidgen) && mv -- "$i" "$uuid.${i##*.}"
done
