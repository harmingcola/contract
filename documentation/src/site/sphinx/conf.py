import sys
import os

extensions = [
    'sphinx.ext.intersphinx',
    'sphinx.ext.viewcode',
]

master_doc = 'index'
copyright = 'Seekay'

html_theme = 'sphinx_rtd_theme'
html_show_sphinx = False

html_static_path = ['css/custom.css', "images"]
html_context = {
    'css_files': [
        '_static/custom.css',  # overrides for wide tables in RTD theme
    ],
}

templates_path = ['_templates']