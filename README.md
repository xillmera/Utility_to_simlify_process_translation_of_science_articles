# «Utility_to_simlify_process_translation_of_science_articles»

## Description

Parser get text separated with spaces to break paragraph by sentences and return modified cascade of text.

Aim of this program to simplify process of translation of text manually done by operator and prepare data structure to parse and processing in future stages (with end at LaTeX compiler).

Main problem to recognise end of sentences only without functional points for special purposes like float numbers presentation. To do so concept of recognising core that have parametres that changing during process of file reading was used. Main problem of this part of program work is chose necessary parametres, set their computing and chose right sequence of checks to return answer of role of point at their place in sentence.

project folder: «..\src\sciencearticleparser»

## Features

- sentence recognision 

## Input and output

Input text can be in different types:

It can be copied from PDF text. It often has hyphenation and parts of one sentence placed in different lines on file.

Or comlete paragraph located in one sentense.

Output text has structure.

1. each sentence placed in different lines

2. thay have : number of paragraph and sentence, also as special structures ordinary to translator. 

### Input data example

```simple
They observed
that the better ordering of alumina nanopores occurs in second-step
anodization if ﬁrst-step anodization is performed for longer duration
owing to better pre-texturing of the aluminum beneath the pore
bottoms, at the metal–oxide interface and the applied temperature
has no eﬀect on ordering of nanopores for a short time anodization.
Stepniowski et al [21] mentioned that interpore distance between
nanopores linearly scales with applied voltage while it increases
exponentially with molar fraction of chromic acid electrolyte.

Most researchers synthesized self-organized nanoporous aluminum
oxide ﬁlms by utilizing the combination of high purity aluminum
substrate and low temperatures (0-10 °C) by using DC anodization
[24–27]. This combination is generally preferred as the existence of any
chemical impurities shall not allow sustainable growth of oxide ﬁlm
owing to thermally enhanced dissolution and dielectric breakdown [28]
at the metal-oxide interface during DC anodization. However, there has
been signiﬁcant interest among the aluminum industry for alternating
current (AC) and pulsed current anodization to fabricate nanoporous
AAO ﬁlms on commercial grade Al-alloys and low purity aluminum
substrates under ambient environment [29–33]. 
```

### Output data example

    25.  They observed that the better ordering of alumina nanopores occurs in second-step anodization if ﬁrst-step anodization is performed for longer duration owing to better pre-texturing of the aluminum beneath the pore bottoms, at the metal–oxide interface and the applied temperature has no eﬀect on ordering of nanopores for a short time anodization. 
        [st^ : ]>[end^ : ]
        Незнакомые слова (перевод) : 
    
        Термины (определение, ан+рус) : 
    
        Перевод фрагмента : 
    
        ИСХ >  They observed that the better ordering of alumina nanopores occurs in second-step anodization if ﬁrst-step anodization is performed for longer duration owing to better pre-texturing of the aluminum beneath the pore bottoms, at the metal–oxide interface and the applied temperature has no eﬀect on ordering of nanopores for a short time anodization. 
    
        МАШ > 
    
        КОН > 

### Prospect of future changes

Add parts to complete cycle of getting ended document with all stages.

It will be next features:

- user interfase different for stages of processing 

- change output format text preperation block to «csv»

- file system examination at some stages

- auto parsing text from «PDF» file with predefined type of text with charactetistic text parametres and spesial parts with unique processing 

- set processig for unique situations, for example by config file

- another parts of document preparation like forming parts of LaTeX document

- auto LuaLaTeX compilation after preparation all requared «TEX» files (requires «texLive» TeX distributive)

- exchange by messages between parts of program with pre-defined manifest 

- and more ... (detailing in each features to real to process parts of work)