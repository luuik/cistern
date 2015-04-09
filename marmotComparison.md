## Introduction ##

On this page you can find comparisons between MarMoT and other published models. All MarMoT models are using the default hyper-parameters and their accuracies are averaged over at least 5 independent runs.

## Silfverberg 2014 ##

The following table shows accuracies for MarMoT and the model proposed by Silfverberg (2014). Note that their models is called CRF in the paper but trained using the averaged perceptron algorithm.

|          | MarMoT | CRF(2,1) |
|:---------|:-------|:---------|
| English  | **97.23** (88.85) | 97.15 (**89.04**) |
| Finnish  | **89.84** (**68.44**) | 88.68 (63.62) |
| Romanian | **97.46** (**86.87**) | 97.29 (86.25) |
| Czech    | **91.68** (76.55) | 91.00 (**77.75**) |
| Estonian | 93.94   (79.50) | **94.01** (**79.53**) |

## References ##

  * Miikka Silfverberg; Teemu Ruokolainen; Krister Lindén; Mikko Kurimo. [Part-of-Speech Tagging using Conditional Random Fields: Exploiting Sub-Label Dependencies for Improved Accuracy](http://aclweb.org/anthology/P/P14/P14-2043.pdf). ACL. 2014