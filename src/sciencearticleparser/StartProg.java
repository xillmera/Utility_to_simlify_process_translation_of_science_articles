package sciencearticleparser;

public class StartProg {
	public static void main(String[] args) {
		String[] basePathToFiles = new String[] {
			"", // pdf file
			"C:\\Users\\RoBoTeR\\Documents\\[main] workplace\\main_Java\\my_programms\\ScienceArticleParser\\пример входных данных\\1. текст из pdf.txt", // file with text from pdf
			"C:\\Users\\RoBoTeR\\Documents\\[main] workplace\\main_Java\\my_programms\\ScienceArticleParser\\пример входных данных\\2. фрагментированный текст.txt", // fragment text file
			"C:\\Users\\RoBoTeR\\Documents\\[main] workplace\\main_Java\\my_programms\\ScienceArticleParser\\пример входных данных\\3. таблица из фрагментов.txt"
		};
		TextParser tp = new TextParser(basePathToFiles);
		tp.executionSelector(TextParser.ParseMode.TranslateFromTextParser);

	}
}
