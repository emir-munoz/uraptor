# LD4IE Challenge 2014 - MicroRaptor (μRaptor)

## Abstract 

This paper describes μRaptor, a DOM-based method to extract hCard microformats from HTML pages 
stripped of microformat markup. μRaptor extracts DOM sub-trees, converts them into rules, and 
uses them to extract hCard microformats. Besides, we use co-occurring CSS classes to improve 
the overall precision. Results show 0.96 precision and 0.83 F1 measure by considering only the 
most common tree patterns. Furthermore, we propose the adoption of additional constraint rules 
on the values of hCard elements to further improve the extraction.

## What μRaptor does?

μRaptor was created to extract [hCard microformats](http://microformats.org/wiki/hcard) from webpages without explicit markup.
μRaptor was submitted to [Linked Data for Information Extraction Challenge](http://data.dws.informatik.uni-mannheim.de/LD4IE/) 
at LD4IE 2014. Thus, you can use μRaptor to *extract* hCard microformats from HTML pages, and *evaluate* your extracted n-quads 
comparing them against the ones extracted by Apache Any23.

## Installation

Download μRaptor from its GitHub [repository](https://github.com/emir-munoz/uraptor).

```
	git clone https://github.com/emir-munoz/uraptor.git
```

μRaptor uses Maven to create an executable file.

```
	mvn clean package
```

You can package the project and use the scripts generated in folder `target/ld4ie-cli/bin`.

## How to use μRaptor?

```
Usage: uRaptor [options]
  Options:
    -evaluate
       Run evaluation of RDF n-quad files
    -extract
       Run microformat extraction
    -gold
       [Evaluate] Gold standard n-quad file
    -input
       [Extract] Input HTMLs file
    -model
       [Evaluate] Extracted n-quad file
    -output
       [Extract] Output N-Quads file
```

To execute an extraction, indicate the input file from the challenge with the clean HTML pages and a
location for the output n-quads file.

To execute an evaluation, indicate the location of the gold standard file and μRaptor extraction file.

Example:

```
	./uRaptor -extract train1.clean.html.txt.gz train1.out.nq

	or

	./uRaptor -evaluate train.out.nq train1.nq
```

## License

GNU General Public License v3.0

    μRaptor: A DOM-based system with appetite for hCard elements
    Copyright (C) 2014

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

Supports Java 1.6 and 1.7.

<!--
[![Build status](https://travis-ci.org/emir-munoz/uraptor.svg?branch=master)](https://travis-ci.org/emir-munoz/uraptor)
-->
