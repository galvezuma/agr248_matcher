# agr248_matcher
Short scripts for perfect search of short nucleotide or amino acid sequences. It uses a parallel approximation to reduce execution time. It requires all the chromosomes to fit in memory. It has been tested in an HP Proliant 585DL with 24 Opteron cores and 256 MiB of memory.

Main program searchs for sequences in ungapped mode with a maximum of mismatches. The next information is hardcoded:
* Number of maximum mismatches.
* Location of file with markers' sequences.
* Location of chromosomes in fasta format.

The names of the chromosomes are in the form 1A, 1B, 1D, 2A, 2B, and so on. Only one fasta sequence is allowed per fasta file.
The format of the file with the markers' sequences is like:
<p><code>DArT3119	TGCAGGGGAAGGAATGCCTAGGGTGTCGCCTGGGCCTAAATGGTCTGGCTGCGGATGGCCTATAGGCCG</code><br/>
<code>DArT3145	TGCAGACGCCCACCTCCGTTCGTTGGTGCCCGTCGCCGTGGGATTTTCCCTCGCGCCACCGAGATCGGA</code><br/>
<code>DArT3146	TGCAGTAAACACAGCTTAATACTACTGCTCTCCGTGTGCGAATGCCGCAACTGCACAAGCTATCAGCGT</code><br/>
<code>DArT3150	TGCAGCCGCCGTGCGCCGTTGGCTGCATCGGCCCGAACCGAGATCGGAAGAGCGGTTCAGCAGGAATGC</code><br/>
<code>.....</code></p>

This program does not check for any input errors and it is intended to use as part of a Netbeans project. Any input error will be shown in the form of an Exception.
