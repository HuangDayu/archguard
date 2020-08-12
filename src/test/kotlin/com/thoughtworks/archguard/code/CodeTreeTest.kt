package com.thoughtworks.archguard.code

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CodeTreeTest {
    @Test
    internal fun `add nodes`() {
        val codeTree = CodeTree()
        codeTree.addClass("a.b.d")
        codeTree.addClass("a.b.e.f")
        codeTree.addClass("a.c")
        codeTree.addClass("m.n.p")
        codeTree.addClass("m.q")
        codeTree.addClass("x")

        assertThat(codeTree.trees.keys).containsExactlyInAnyOrderElementsOf(listOf("a", "m", "x"))

        assertThat(codeTree.trees.get("a")).isEqualTo(Node("a", TypeEnum.PACKAGE))
        assertThat(codeTree.trees.get("a")!!.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("b", TypeEnum.PACKAGE), Node("c", TypeEnum.FILE)))
        assertThat(codeTree.trees.get("a")!!.children.first { it.node == "b" }.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("d", TypeEnum.FILE), Node("e", TypeEnum.PACKAGE)))
        assertThat(codeTree.trees.get("a")!!.children.first { it.node == "b" }.children.first { it.node == "e" }.children)
                .containsOnly(Node("f", TypeEnum.FILE))

        assertThat(codeTree.trees.get("m")).isEqualTo(Node("m", TypeEnum.PACKAGE))
        assertThat(codeTree.trees.get("m")!!.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("n", TypeEnum.PACKAGE), Node("q", TypeEnum.FILE)))
        assertThat(codeTree.trees.get("m")!!.children.first { it.node == "n" }.children)
                .containsOnly(Node("p", TypeEnum.FILE))
        assertThat(codeTree.trees.get("x")).isEqualTo(Node("x", TypeEnum.FILE))
    }
}