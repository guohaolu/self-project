# self-algorithm
本人的数据结构与算法学习历程。

<!-- TOC -->
* [self-algorithm](#self-algorithm)
  * [base-algorithm](#base-algorithm)
  * [algorithm-design](#algorithm-design)
  * [business-algorithm](#business-algorithm)
    * [git diff原理](#git-diff原理)
<!-- TOC -->

## base-algorithm
基础算法
## algorithm-design
算法设计——康奈尔大学
## business-algorithm
《业务开发算法50讲》by：黄清昊

### git diff原理
文本差分算法，默认为Myers算法，其核心思想是通过比较两个文本的字符，从而得到两个文本的最少编辑距离。
文本A通过`编辑脚本`（edit script）得到文本B。

编辑脚本：新增操作和删除操作的序列。

算法评价指标：编辑脚本的长度最小。
