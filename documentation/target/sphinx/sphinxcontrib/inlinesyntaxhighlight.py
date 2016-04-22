from docutils.parsers.rst.roles import register_canonical_role, set_classes
from docutils.parsers.rst import directives
from docutils import nodes
import re

def code_role(role, rawtext, text, lineno, inliner, options={}, content=[]):
    r'''code_role override or create if older docutils used.

    This only creates a literal node without parsing the code. This will
    be done later in sphinx.  This override is not really needed, but it 
    might give some speed
    '''

    set_classes(options)

    language = options.get('language', '')
    classes = ['code']

    if 'classes' in options:
        classes.extend(options['classes'])

    if language and language not in classes:
        classes.append(language)

    node = nodes.literal(rawtext, text, classes=classes, language=language)
    
    #import rpdb2 ; rpdb2.start_embedded_debugger('foo')

    return [node], []

code_role.options = { 'class': directives.class_option,
                      'language': directives.unchanged }

register_canonical_role('code', code_role)


from sphinx.writers.html import HTMLTranslator, BaseTranslator

DIV_PRE_RE = re.compile(r'^<div[^>]*><pre>')
PRE_DIV_RE = re.compile(r'\s*</pre></div>\s*$')

def html_visit_literal(self, node):
    env = self.builder.env

    shall_highlight = False


    if node.rawsource.startswith('``') or 'code' in node['classes']:
        #if node.rawsource != node.astext():
            # most probably a parsed-literal block -- don't highlight
            #return BaseTranslator.visit_literal(self, node)


        if env.config.inline_highlight_respect_highlight:
            lang = self.highlightlang
        else:
            lang = None

        highlight_args = node.get('highlight_args', {})

        if node.has_key('language'):
            # code-block directives
            lang = node['language']
            highlight_args['force'] = True

        def warner(msg):
            self.builder.warn(msg, (self.builder.current_docname, node.line))

        # determine if shall_highlight
        shall_highlight = True

        #import rpdb2 ; rpdb2.start_embedded_debugger('foo')

        attrs = node.attributes

        if 'role' in attrs: # e.g. for :file:`...`
            shall_highlight = False

        elif 'code' in attrs['classes'] and attrs.get('language'):
            shall_highlight = True

        else:
            shall_highlight = env.config.inline_highlight_literals   ## DEFAULT FROM SETTINGS

            if (
                shall_highlight and
                env.config.inline_highlight_respect_highlight and 
                not attrs.get('language')
               ):
                lang = self.highlightlang

        if not lang:
            shall_highlight = False

    if shall_highlight:

        highlighted = self.highlighter.highlight_block(
            node.astext(), lang, warn=warner, **highlight_args)

        # highlighted comes as <div class="highlighted"><pre>...</pre></div>

        highlighted = DIV_PRE_RE.sub('', highlighted)
        highlighted = PRE_DIV_RE.sub('', highlighted)

        # import rpdb2 ; rpdb2.start_embedded_debugger('foo')

        starttag = self.starttag(node, 'code', suffix='',
                                 CLASS='docutils literal highlight highlight-%s' % lang)
        self.body.append(starttag + highlighted + '</code>')

    else:
        self.body.append(self.starttag(node, 'tt', '', CLASS='docutils literal')
            + node.astext() + '</tt>')

#    self.protect_literal_text += 1

    raise nodes.SkipNode


def html_depart_literal(self, node):
    #self.protect_literal_text -= 1
    #self.body.append('</tt>')
    pass

import types


HTMLTranslator.visit_literal  = html_visit_literal
HTMLTranslator.depart_literal = html_depart_literal


        # highlighted = self.highlighter.highlight_block(
        #     node.rawsource, lang, warn=warner, linenos=linenos,
        #     **highlight_args)
        # starttag = self.starttag(node, 'div', suffix='',
        #                          CLASS='highlight-%s' % lang)
        # self.body.append(starttag + highlighted + '</div>\n')
        # raise nodes.SkipNode

def setup(app):
    app.add_config_value('inline_highlight_literals', True, 'env')
    app.add_config_value('inline_highlight_respect_highlight', True, 'env')

    # add option if ``...`` (literal without node.role attribute, shall be highlighted)
