package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PORTRAIT;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PortraitCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class PortraitCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the PortraitCommand
     * and returns an PortraitCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PortraitCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PORTRAIT);
        if (!arePrefixesPresent(argMultimap, PREFIX_PORTRAIT)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PortraitCommand.COMMAND_WORD));
        }
        
        Index index;
        String filePath;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            filePath = argMultimap.getValue(PREFIX_PORTRAIT).get();
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PortraitCommand.MESSAGE_USAGE));
        }

        return new PortraitCommand(index, filePath);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
