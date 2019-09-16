#!/bin/bash

pandoc Requirements-Analysis.md -f markdown -t html -s -o temp.html
pandoc temp.html -f html -t odt -o Requisitos.odt
rm temp.html
