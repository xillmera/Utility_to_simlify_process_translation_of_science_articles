package sciencearticleparser;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class TextParser {
	// ?? Как используется класс ??
	// Чтобы запустить механизм извлечения текста сначала создается экземпляр класса.
	// Выбор механизма работы происходит с помощью pathTypeSelectionAndExecution.
	// Но исполнение невозможно до заполнения необходимых переменных, поэтому при каждой команде.
	// Исполнения запускается тест на доступность и правильность переменных.
	// К примеру если требуется файл pdf, то в конце обязательно должно быть указанное расширение файла
	// и соответственно занимая последнии символы в пути файла.
	// В противном случае переменная повреждена и нужно запросить ее перезапись.
	// Все остальные методы в классе должны быть недоступны.
	// Для некоторых случаев, когда в конструкторе я помещаю пути к файлам, можно запустить извлечение текста сразу в конструкторе.
	// После исполнения любого механизма результат работы будет доступен с помощью отдлеьных методов.

	// переменные для хранения пути к Pdf, полученному из него тексту и файлу для результирующего текста.
	// TODO новые файлы добавляются сюда
	private String pathToPDF;
	private String pathToSFormText; // минимально отформатированный текст
	private String pathToFragText;
	// все типы файлов используемых в программе
	private enum FormatMode { PDFformat, TXTformat }
	// размеры массивов должны друг другу соответствовать.
	// дискретизация режимов работы
	// TODO Все массивы ниже изменяются единовременно
	public enum PossibleFilePath { PDFfile, SFormTextFile , FragTextFile , TranslFrFrag, All} // длинна всегда на 1 больше исходного массива
	private PossibleFilePath[] fileApplication = new PossibleFilePath[]{PossibleFilePath.PDFfile, PossibleFilePath.SFormTextFile, PossibleFilePath.FragTextFile, PossibleFilePath.TranslFrFrag};
	private String[] pathToFiles = new String[]{null, null, null, null};
	private boolean[] havePathToFiles = new boolean[] {false,false,false,false};
	private FormatMode[] filesType = new FormatMode[] {FormatMode.PDFformat, FormatMode.TXTformat, FormatMode.TXTformat, FormatMode.TXTformat};

	private void displayPathState(){
		int len = havePathToFiles.length; //понимается что все массивы одинаковой размерности.
		for (int i = 0; i<len ; i++){
			System.out.print(fileApplication[i]+" : " + havePathToFiles[i] + " ; ");
		}
		System.out.println();
	}

	public enum FilePathChangeMode { Set, Clear, Info, error }
	public void changeFilesPath (FilePathChangeMode changeMode, String filePath, PossibleFilePath type){
		// проходит один раз до конца

		System.out.println("// Parser : change files path : Start (изменение путей к файлам)");

		if ( havePathToFiles.length != filesType.length && havePathToFiles.length != pathToFiles.length) {
			System.out.println("Parser : change files path : ошибка в коде, массивы не одинаковой длины");
		}

		int
			stCounter = 0,
			edCounter = pathToFiles.length;

		switch (changeMode){
			case Set : // устанавливает значение пути без проверки.
				System.out.println("// Parser :sfp: setPath (назначение пути) :  ");
				Integer num = null;
				switch (type){
					case PDFfile :  num = 0; break;
					case SFormTextFile : num = 1; break;
					case FragTextFile : num = 2; break;
					case TranslFrFrag : num = 3; break;
					case All :
						System.out.println("Parser :sfp:: Операция присвоения одинакового пути всем файлам не имеет смысла!");
						break;
				}
				// нужно следить за размером filesType - он должен быть равер размеру массива файлов
				if (num != null){
					if (readyToUseTest(filePath, filesType[num])) {
						pathToFiles[num] = filePath ;
						havePathToFiles[num] = true;
						System.out.println("|| Parser :sfp:: Путь _"+ filePath+"_ присвоен успешно");
					} else {
						System.out.println("|| Parser :sfp:: Путь _"+ filePath+"_ не присвоен");
					}

				} else {
					//skip
					System.out.println("|| Parser :sfp:: или не выполнено или не выбран файл для присвоения пути или не установлен в программе");
				}
			break;
			case Clear : // очищает необходимые пути
				System.out.println("// Parser :sfp: Clear (освобождение пути) :  ");
				switch (type) {
					case PDFfile:
						stCounter = 0;
						edCounter = 1;
						break;
					case SFormTextFile:
						stCounter = 1;
						edCounter = 2;
						break;
					case FragTextFile:
						stCounter = 2;
						edCounter = 3;
						break;
					case TranslFrFrag:
						stCounter = 3;
						edCounter = 4;
						break;
					case All: // значения по дефолту
						break;
				}
				// массивы должны быть одинаковых размеров
				while (stCounter < edCounter) {
					pathToFiles[stCounter] = null;
					havePathToFiles[stCounter++] = false;
				}
				System.out.println("|| Parser :sfp:cl: end ");
			break;
			case Info :
				System.out.println("// Parser :sfp: Info (информация о путях) :  ");
				String[]
					firstMessage = new String[]{" PDF файлу ", " TXT файлу с подготовленным текстом ", " TXT файлу с текстом разбитым на фрагменты "},
					secondMessage = new String[]{" PDF файлу - ", " TXT файлу с подготовленным текстом -  ", " TXT файлу с подготовленным текстом - "};

				switch (type) {
					case PDFfile:
						stCounter = 0;
						edCounter = 1;
						break;
					case SFormTextFile:
						stCounter = 1;
						edCounter = 2;
						break;
					case FragTextFile:
						stCounter = 2;
						edCounter = 3;
						break;
					case TranslFrFrag:
						stCounter = 3;
						edCounter = 4;
						break;
					case All:
						stCounter = 0;
						edCounter = 3;
						break;
				}
				// массивы должны быть одинаковых размеров
				int counter = stCounter;
				while (stCounter < edCounter) {
					if (pathToFiles[stCounter] == null)
						System.out.println("|| Parser :sfp:inf: путь к" + firstMessage[stCounter] + "_не указан_.");
					else
						System.out.println("|| Parser :sfp:inf: путь к" + secondMessage[stCounter] + filePath);
					stCounter++;
				}
				System.out.println("// Parser :sfp:inf: end ");
			break;
		}
		System.out.println("\\\\ Parser :sfp: out");
	}

	private boolean readyToUseTest (String filePath, FormatMode executionMode) {
		// проверка на доступность файла
		// заполнена ли переменная \ правильно ли записана сама переменная
		// имеет 2 режима работы : для пдф (1) и текстового файла(2)

		// Стандартная проверка первое - проверить доступность для чтения файл.
		if (filePath != null) {
			try (Reader reader = new StringReader(filePath)) {
				// попытка доступа к файлу на чтение
			} catch (FileNotFoundException ex2 ){
				System.out.println("|| Parser ::rtuTst: "+ex2+" : Файл не найден.\nПуть : " + filePath  + "\nМожет быть поврежден.\n" +
					"Проверьте правильность пути к файлу.\n Для работы программы введите работоспособный путь.");
				return false;
			}catch (IOException ex) {
				// файл не может быть считан
				System.out.println("|| Parser ::rtuTst: "+ex+" : Файл не может быть считан.\nПуть : " + filePath  + "\nМожет быть поврежден.\n" +
					"Проверьте правильность пути к файлу.\n Для работы программы введите работоспособный путь.");
				return false;
			}
		} else if (filePath.equals("")){
			// переменная к файлу пустая
			System.out.println("|| Parser ::rtuTst: Путь к файлу не заполнен");
			return false;
		}

		int len = filePath.length();
		if (len < 5) return false;  // не может быть пути к файлу меньше чем расширение этого файла
		String testedString = filePath.substring(len - 4);
		switch (executionMode) {
			case PDFformat:
				// проверка на наличие расширения пдф файла в конце строки
				if (!testedString.equals(".pdf")) {
					System.out.println("|| Parser ::rtuTst: Файл имеет (неверное) отличное от \".pdf\" расширение");
					return false;
				}
				break;
			case TXTformat:
				// проверка на наличие расширения текстового файла в конце строки
				// нужно строку преобразовать в массив символов и в обратном порядке проверить символы.
				if (!testedString.equals(".txt")) {
					System.out.println("|| Parser ::rtuTst: Файл имеет (неверное) отличное от \".txt\" расширение");
					return false;
				}
				break;
		}
		return true;
	}

	public enum ParseMode { PdfParser, FragmentsFromTextParser, TranslateFromTextParser }
	private boolean readyToParseByModeTest (ParseMode mode){
		switch (mode){
			case PdfParser:
				if (havePathToFiles[0]){
					System.out.println("|| Parser :RTParseBMTest::: Файлы готовы для режима "+mode);
					return true;
				} else {
					System.out.println("|| Parser :RTParseBMTest::: Не все файлы готовы для режима "+mode);
					displayPathState();
				}
				break;
			case FragmentsFromTextParser:
				if (havePathToFiles[1] && havePathToFiles[2]){
					System.out.println("|| Parser :RTParseBMTest::: Файлы готовы для режима "+mode);
					return true;
				} else {
					System.out.println("|| Parser :RTParseBMTest::: Не все файлы готовы для режима "+mode);
					displayPathState();
				}
				break;
			case TranslateFromTextParser:
				if (havePathToFiles[2] && havePathToFiles[3]){
					System.out.println("|| Parser :RTParseBMTest::: Файлы готовы для режима "+mode);
					return true;
				} else {
					System.out.println("|| Parser :RTParseBMTest::: Не все файлы готовы для режима "+mode);
					displayPathState();
				}
				break;
		}
		return false;
	}

	public void executionSelector (ParseMode executionMode){
		// 1 : pdt > txt; 2 : txt > frags; 3 : txt > rus\engl
		// Все проверки правильности путей происходят только в данном методе (при попытке исполнить).
		System.out.println("\n// Parser: executionMode : "+executionMode);
		switch (executionMode) {
			case PdfParser:
				// входной файл пдф, выходной текст - получить текст
				// проверить наличие двух переменных иначе сказать
				break;
			case FragmentsFromTextParser:
				// входной файл текст, выходной текст - разбить текст из пдф на фрагменты
				// входной текст должен быть правильно отформатирован
				if (readyToParseByModeTest(ParseMode.FragmentsFromTextParser)){
					// проверка прошла успешно.
					// выполнение механизма.
					getFragmentsFromText();
					System.out.println("|| Parser :exeMD:: фрагменты записаны в файле.\n\tРезультат работы досупен для считывания.");
				} else {
					System.out.println("|| Parser :exeMD:: не все файлы готовы. Заполните требуемые");
				}

				break;
			case TranslateFromTextParser:
				// входной файл текст, выходной текст - выделить из фрагментов ячейки
				// форматирование под таблицу. (возможно запись в csv)
				if (readyToParseByModeTest(ParseMode.TranslateFromTextParser)){
					// проверка прошла успешно.
					// выполнение механизма.
					TranslateFromTextParser();
					System.out.println("|| Parser :exeMD:: фрагменты записаны в файле.\n\tРезультат работы досупен для считывания.");
				} else {
					System.out.println("|| Parser :exeMD:: не все файлы готовы. Заполните требуемые");
				}
				break;
		}
		System.out.println("\\\\ Parser: exeMD: out");
	}

	public TextParser (String[] amountOfPathes) {
		// в конструкторе я пробую заполнить все пути к файлам через встроенную функцию.
		// при этом на вход подается массив строк в котором возможно указывать пустые строки или битые
		// в таком случае они просто не зарегистрируются в парсере.
		// но главное подать массив той же размерности что и нужно.
		int
			len = pathToFiles.length,
			counter = 0;
		if(amountOfPathes.length!=len) {
			System.out.println("Parser : подан массив неподходящей длинны. Запрашиваемая : " + len);
			return;
		}

		// попытка занести все значения файлов во все поля.
		while (counter < len){
			changeFilesPath(FilePathChangeMode.Set, amountOfPathes[counter], fileApplication[counter]);
			counter++;
		}
	}
	public TextParser (){// noting to do
	}

	private void getTextFromPdf() {
		// метод получает ПДФ файл и парсит из него текст
		// в тоже время подготавливает текст для разделения на фрагменты
		// дальнейшая реализация - основана на сторонних библиотеках по работе с пдф.
	}

	private final int maxEmptyLineSize = 2;//критерий - можно менять
	private boolean isEmptyLine(String line){
		if (line.equals("")) {
			return true;
		} else if (line.length() <= maxEmptyLineSize) {
			return true;
		}
		return false;
	}

	private void getFragmentsFromText (){
		// метод парсит текст на фрагменты
		// на вход нужен текст с определенным форматированием
		// Выходные даннные могут быть различны в зависимости от типа манипуляций с фрагментом данных
		//TODO для фрагмента передается номер между предложениями для кусков.

		// я могу каждый фрагмент записать в коллекцию или по завершению фрагмента сразу его записывать в файл.
		// в первом случае требуется память для хранения всего текста, что затрудняется при больших объемах.
		// однако должно существовать 2 метода по извлечению фрагментов и по их записи
		// во втором случае я порционно решаю проблему, однако тогда метод превращается в transport \ transmitter (перевозчика), но сводится до одного

		/*
		Reader - механизм чтения файла.
		DataFrag - самописный класс фрагмента данных
		TreeMap - коллекция для хранения фрагментов
		TestForSnts - самописный класс для проверки разделителя.
			должен инициализироваться глобально, чтобы иметь межстрочную доступность.
		*/
		try (BufferedReader rd = new BufferedReader(new FileReader(pathToFiles[1]));
		     		Writer wr = new BufferedWriter(new FileWriter(pathToFiles[2]) ) ) { // в конце фрагмента я должен записать предыдущий

			System.out.println("// Parser : fragText : Start");

			Integer
				lastFragNum = 1,//между фрагментами передается конструктором
				lastSentNum = 1;
			DataFrag df = new DataFrag(lastFragNum, lastSentNum);

			//TreeMap tm = new TreeMap(); для продолжительного накапливания фрагментов
			TestForSentense tfs = new TestForSentense();
			/*
			emptyLines - Счетчик количества пройденных строк пустого типа.
			fullLines - Счетчик количества пройденных строк одного типа.
			abillityToParse - Индикатор состояния. Определяет запуск алгоритма по разделению текста
			*/
			int
				emptyLines = 0,
				fullLines = 0,
				sentPerFrag = 0;
			boolean
				abillityToParse = true,
				lastLineNotSentense = true;
			String line = ((BufferedReader) rd).readLine() ;

			// цикл для прохода по всему файлу
			while (line != null) {        //пока не достигнет конца файла

				int
					lineLength = line.length(),
					separatorPoint = 0;

				boolean nowEmptyLine = isEmptyLine(line);
				if (nowEmptyLine) emptyLines++;
				else emptyLines = 0;

				//парсер работает по следующему алгоритму:
				// На вход подается текст где каждый заголовок и абзац отделены друг от друга пустой строкой
				// таким образом формируя фрагменты.
				// Для текста характерно последовательное(друг за другом) расположение строк
				// с переходом в конце на пустую строку.
				// Количество пустых строк означает режим работы.
				// Одна строка - следующий фрагмент обрабатывается, две строки - ждать три строки
				// и не обрабатывать текст в промежутке совсем - как бы выделение нерабочего фрагмента.

				//TODO нужно как-то определить конец файла и перед выходом записать оставшееся во фрагменте (граничные условия)

				// проверка пустой строки и работоспособности.
				// line.equals("") - рабочий вариант проверки строки на пустую, однако могут встречаться пробелы, поэтому лучше сделать отдельную проверку
				if (abillityToParse && emptyLines == 2){
					abillityToParse = false;
				}else if (!abillityToParse && emptyLines > 2) {
					// при нахождении трех пустых строк подряд
					abillityToParse = true;
				} else if (abillityToParse && nowEmptyLine) {

					// механизм перехода между фрагментами и пропускания работы парсера для выделенных строк
					if (emptyLines == 1) {
						// впервые встречает пустую строку
						// нужно завершить запись данных опредленного фрагмента.
						// Внести фрагмент в хранилище и создать новый фрагмент.

						tfs = new TestForSentense(); // обнуляет параметры.
						sentPerFrag = 0;
						if (df.wasUsed()) { //чтобы избежать пустых строк в начале

							if (lastLineNotSentense) lastSentNum++;
							df.addSentense(); //записываю последнее распознанное предложение.
							//lastSentNum++;
							// т.к конец фрагмента, то записываю его в файл и пересоздаю
							wr.append(df.toString());
							df = new DataFrag(++lastFragNum, lastSentNum);
						}
						fullLines = 0;
					}

				} else if (abillityToParse && !nowEmptyLine){
					// для всего текста одного фрагмента
					fullLines++;
					emptyLines = 0;


					while (separatorPoint != lineLength) { // цикл для одной строки. выход по достижению конца строки
						if (separatorPoint != 0) { //недетерминированное значение говорит о результатах проверки
							line = line.substring(separatorPoint + 1, lineLength);
							//+1 потому что точка остается в занессенной в фрагмент строке
							lineLength = line.length();
							separatorPoint = 0;
						}

						if (tfs.sentenseTest(line)) {
							// Если точка есть то нужно получть ее место, разрезать строку, поставить
							// новую проверку и отправить часть до точки в фрагмент данных.
							// Все задействованные внутри проверки переменные обнуляются в ней же.
							//добавляет предложение в конец экземпляра данных
							separatorPoint = tfs.getResultSep(); //либо получаю результат
							df.addSubSentense(line.substring(0, separatorPoint+1));
							df.addSentense(); // записывает предложение
							lastSentNum++;
							sentPerFrag++;
							lastLineNotSentense = false;

						} else {
							// если нет точек до конца предложения, то нужно записать оставшееся.
							// обнулять при этом параметры не нужно. (выполненно внутри проверки)
							df.addSubSentense(line);
							separatorPoint = tfs.getResultSep(); //либо получаю конец цикла
							lastLineNotSentense = true;
						}
						//tfs.displayParam(); // вариативно
						int diffParam = 4;
						if (lineLength-separatorPoint <= 3) break;

					}


				}
				line = ((BufferedReader) rd).readLine() ;
			}

			System.out.println("|| Parser : fragText : в файле найдено "+lastFragNum + " фрагментов и "+ lastSentNum +" предложений.\n"+
				"\\\\ Parser : fragText : end");

		}catch (IOException io){
			System.out.println("\\\\ Parser : fragText : "+ io.getMessage());
		}
	}

	private void TranslateFromTextParser(){
		try (BufferedReader rd = new BufferedReader(new FileReader(pathToFiles[2]));
		     Writer wr = new BufferedWriter(new FileWriter(pathToFiles[3]) ) ) { // в конце фрагмента я должен записать предыдущий

			System.out.println("// Parser : fragText : Start");

			String line = ((BufferedReader) rd).readLine() ;
			wr.write(
				"\\documentclass[12pt,a4paper]{article}\n" +
				"\\usepackage[utf8]{inputenc}\n" +
				"\\usepackage[left=2.00cm, right=1.00cm, top=2.00cm, bottom=2.00cm]{geometry}\n "+
				"\\usepackage[russian,english]{babel}\n" +
				"\\usepackage[T2A]{fontenc}\n" +
				"\\usepackage{tabularx}"+
				"\\usepackage{adjustbox}\n" +
				"\\usepackage{longtable}\n" +
				"\\usepackage{graphicx}\n" +
				"\\graphicspath{{images/}}\n" +
				"\n" +
				"\\author{Складнев Павел 1032173800}\n" +
				"\\title{Перевод 3й статьи}\n" +
				"\\date{\\today}\n" +
				"\n" +
				"\\begin{document}\n" +
				"\t\\maketitle\n" +
				"\t\\newpage\n" +
				"\t\\begin{longtable}[h!]{|p{0.444\\linewidth}|p{0.556\\linewidth}|}\n");


			// цикл для прохода по всему файлу
			int
				state = 0,
				cntr = 0;
			String
				textRex = "ИСХ >*",
				fragRex = "Frag_*";
			Matcher
				textM = Pattern.compile(textRex).matcher(line),
				fragM = Pattern.compile(fragRex).matcher(line),
				picM = Pattern.compile(fragRex).matcher(line),
				tableM = Pattern.compile(fragRex).matcher(line);
			String
				enTextBuf = "",
				ruTextBuf = "";
			while (line!=null) {        //пока не достигнет конца файла

				boolean
					textExistance = textM.find(),
					fragExistance = fragM.find();

				if (fragExistance && cntr != 0){

					wr.append(
						"\t\t" + enTextBuf +"\n"+
							"\t\t&\n" +
							" \t\t" + ruTextBuf +"\n"+
							"\t\t\\\\\n" +
							"\t\t\\hline\n");
					enTextBuf = "";
					ruTextBuf = "";
					textRex = "ИСХ >*";
				} else if (fragExistance) {
					wr.append("\t\t\\hline\n");
					cntr++;
				}

				if (textExistance && state == 0) {

					enTextBuf  += line.substring(textM.end(), line.length());
					textRex = "КОН >*";
					state = 1;
				} else if (textExistance && state == 1) {
					ruTextBuf  += line.substring(textM.end(), line.length());
					textRex ="ИСХ >*";
					state = 0;
				}

				line = ((BufferedReader) rd).readLine();
				if (line != null) {
					textM = Pattern.compile(textRex).matcher(line);
					fragM = Pattern.compile(fragRex).matcher(line);
				}

			}
			wr.append(
				"\t\t" + enTextBuf +"\n"+
				"\t\t&\n" +
				" \t\t" + ruTextBuf +"\n"+
				"\t\t\\\\\n" +
				"\t\t\\hline\n" +
				"\t\\end{longtable}\n" +
				"\\end{document}");

		}catch (IOException io){
			System.out.println("\\\\ Parser : fragText : "+ io.getMessage());
		}
	}
}
