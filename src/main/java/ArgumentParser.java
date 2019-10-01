import org.apache.commons.cli.*;

import java.io.FileNotFoundException;

public class ArgumentParser {
    public static Arguments parse(String[] args) throws FileNotFoundException {
        // TODO - version
        // TODO - description (lsif-java is an LSIF indexer for Java.)

        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("lsif-java", options);
            System.exit(1);
            return null;
        }

        boolean debug = cmd.hasOption("debug");
        boolean verbose = cmd.hasOption("verbose");
        String projectRoot = cmd.getOptionValue("projectRoot", ".");
        boolean noContents = cmd.hasOption("noContents");
        String outFile = cmd.getOptionValue("out");
        boolean stdout = cmd.hasOption("stdout");

        if (outFile == null && !stdout) {
            System.err.println("either an output file using --out or --stdout must be specified");
            System.exit(1);
        }

        if (stdout && (verbose || debug)) {
            System.err.println("debug and verbose options cannot be enabled with --stdout");
            System.exit(1);
        }

        return new Arguments(projectRoot, noContents, outFile, stdout);
    }

    private static Options createOptions() {
        Options options = new Options();

        options.addOption(new Option(
                "", "debug", false,
                "Display debug information."
        ));

        options.addOption(new Option(
                "", "verbose", false,
                "Display verbose information."
        ));

        options.addOption(new Option(
                "", "projectRoot", true,
                "Specifies the project root. Defaults to the current working directory."
        ));

        options.addOption(new Option(
                "", "noContents", false,
                "File contents will not be embedded into the dump."
        ));

        options.addOption(new Option(
                "", "out", true,
                "The output file the dump is save to."
        ));

        options.addOption(new Option(
                "", "stdout", false,
                "Writes the dump to stdout."
        ));

        return options;
    }
}