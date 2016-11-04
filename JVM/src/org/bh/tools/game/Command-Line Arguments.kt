package org.bh.tools.game

import org.bh.tools.base.collections.filterMap
import org.bh.tools.base.util.times

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for Snek.
 *
 * A complete command-line argument.
 *
 * Attempts to follow guidelines at http://www.gnu.org/software/libc/manual/html_node/Argument-Syntax.html
 *
 * @author Kyli Rouge
 * @since 2016-11-02
 */
data class CompleteCommandLineArg<out ActionOutput>(
        override val singleCharacterArgument: Char,
        override val fullTextArgument: String,
        override val description: String,
        override val action: (Array<String>) -> ActionOutput
) : SingleCharacterCommandLineArg<ActionOutput>, FullTextCommandLineArg<ActionOutput> {
    override val regex: Regex
        get() = Regex("(${super<FullTextCommandLineArg>.regex}|${super<SingleCharacterCommandLineArg>.regex})")
}

interface SingleCharacterCommandLineArg<out ActionOutput> : CommandLineArg<ActionOutput> {
    /**
     * The argument as a single character. This might be combined with others, so `-a -b -c` is the same as `-abc`.
     * Whitespace is not supported.
     */
    val singleCharacterArgument: Char

    /**
     * Matches a singular hyphen, zero or more non-whitespace characters, this command's character, and zero or more
     * non-whitespace characters
     */
    override val regex: Regex
        get() = Regex("$prefix{1}\\S*?$singleCharacterArgument\\S*?\\b")

    companion object {
        val prefix = "-"
    }
}

interface FullTextCommandLineArg<out ActionOutput> : CommandLineArg<ActionOutput> {
    /**
     * The argument as a full-text word. Whitespace is not supported.
     */
    val fullTextArgument: String

    /**
     * Matches two hyphens and the full text argument
     */
    override val regex: Regex
        get() = Regex("$prefix$fullTextArgument\\b")

    companion object {
        val prefix = SingleCharacterCommandLineArg.prefix * 2
    }
}

interface CommandLineArg<out ActionOutput> {
    /**
     * A long, plain-text, human-readable description of what this argument does
     */
    val description: String

    /**
     * The action to be taken if this argument is passed.
     *
     * This also parses any parameters that come after the argument. For instance `-o /usr/foo.txt /var/bar.gz` would
     * pass an array like: `["/usr/foo.txt", "/var/bar.gz"]`
     *
     * @param parameters Any parameters that came after this argument
     */
    val action: (parameters: Array<String>) -> ActionOutput

    /**
     * A regular expression that will match this argument
     */
    val regex: Regex
}

abstract class CommandlineArgCollection {
    abstract val args: Array<CommandLineArg<*>>

    private val _parser: CommandLineArgParser by lazy { CommandLineArgParser(this) }

    fun parse(args: Array<String>) = _parser.parseArgs(args)
}

class CommandLineArgParser(val collection: CommandlineArgCollection) {
    /**
     * Parses the given strings into arguments
     *
     * TODO: Support argument parameters
     */
    fun parseArgs(args: Array<String>): List<CommandLineArg<*>> {
        return args.filterMap { arg ->
            val match = collection.args.firstOrNull { it.regex.matches(arg) }
            Pair(match != null, { match!! })
        }
    }
}
