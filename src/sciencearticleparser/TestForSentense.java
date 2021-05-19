package sciencearticleparser;

public class TestForSentense {
	// все данные критериев храняться внутри класса
	// на качество определения влияет правильность выбранных критериев и их параметров
	// Проверку по критериям отдельно не вызывают. Это делается централизованно в одном методе
	// по умолчанию критерии возващают тру и если проверка не выполняется то false

	private int separatorPoint = 0;
	private int lineLen = 0;

	// межстрочные критерии
	// расстояние до точки определенной как последнее предложение
	private int pointToLstSentPointGap = 0;
	private final int minPointToLstSentPointGap = 30;
	private boolean countPTLSPCriterion () {
		if (pointToLstSentPointGap <= minPointToLstSentPointGap) return false;
		return true;
	}

	// Расстояние до последней точки
	private int pointToPointGap = 0;
	private final int minPointToPointGap = 30;
	private boolean countPTPCriterion () {
		return true; // пока не понимаю необходимости.
		/*
		if (pointToPointGap <= minPointToPointGap) return false;
		return true;*/
	}

	// локальные критерии. Нужно высчитывать в каждом предложении
	// расстояние до последнего пробела : pointToSpaceGap
	private final int minPointToSpaceGap = 3; //параметр критерия : максимально допустимое количество символов
	private boolean countPTSCriterion (char[] charOfLine, int sepPoint) {
		if (sepPoint <= minPointToSpaceGap) return true; //за расстояние от начала стоки отвечает другой критерий
		if (charOfLine[sepPoint-1] == ' ') return true; // может быть ошибка отделения точки в конце предложения (не ясно)
		int gapToSpace = -1;
		do {
			if (charOfLine[sepPoint] == ' ')
				break;
			else gapToSpace++;
		} while (sepPoint-- != 0);

		if (gapToSpace <= minPointToSpaceGap) return false;
		return true;
	}

	// количество предложений в одной строке : не должно быть больше параметра(n) : maxSentAmount
	private int sentAmount = 0;
	private final int maxAmountOfSentenses = 1; //параметр критерия : возможное количество предложений в строке
	private boolean countMxSAСriterion () {
		if (sentAmount > maxAmountOfSentenses) return false;
		sentAmount++;
		return true;
	}
	// наличие перед точкой символов ')', ']' : не должно быть ни одного из них
	private boolean countBCСriterion (char pastChar) {
		if (pastChar == '.') return false;
		else if (pastChar == ')') return false;
		else if (pastChar == ']') return true;
		return true;
	}
	// проверка наличия перед точкой цифры : не должно быть цифры совсем
	private boolean countNLCСriterion (char pastChar) { // последний символ перед разделителем
		if (pastChar == '.') return false;
		boolean isNum =
			pastChar == '0' ||
			pastChar == '1' ||
			pastChar == '2' ||
			pastChar == '3' ||
			pastChar == '4' ||
			pastChar == '5' ||
			pastChar == '6' ||
			pastChar == '7' ||
			pastChar == '8' ||
			pastChar == '9';
		if (isNum) return false;
		return true;
	}
	// проверка расстояния от точки до начала строки : не должно быть меньше параметра(n) : pointToLineStGap
	private int minPointToLineStGap = 4;
	private boolean countPTLSСriterion (int sepPoint) { //текущее значение исследуемого символа в строке
		return sepPoint >= minPointToLineStGap ? true : false;
	}
	//критерий ПРЕДЛОЖЕНИЯ в одно слово (строка маленькая )
	private int minOneLineLong = 5; // для отделения от пустых строк
	private int maxOneLineLong = 30;
	private boolean countSzСriterion (String studiedLine) { //текущее значение исследуемого символа в строке
		int len = studiedLine.length();
		boolean res = compareIdentity(studiedLine);
		if (minOneLineLong <= len && len <= maxOneLineLong){
			return !res ? true : false;
		}
		return false;
	}
	// нужны критерии оценивающие размер предложения

	private String studiedLine = ""; //строка для проверки



	private boolean result = false;
	public int getResultSep (){
		// после расчета можно получить результат отдельным методом
		// Результат бессмысленно вызывать когда проверка не пройдена
		if (result) return separatorPoint;
		else {
			//System.out.println("TFS: результат недоступен. Возвращаю размер входной строки");
			return lineLen;
		}
	}

	//TODO убрать после отладки
	private boolean[] params = new boolean[7];
	public void displayParam (){
		System.out.println("|| Parser :fragText:tfs:SentParams: "+params[0]+params[1]+params[2]+
			params[3]+params[4]+params[5]+params[6] +"; sep : "+separatorPoint + " ; len : "+ lineLen);
	}

	private String lastIdentity = "";
	private  void setLastIdentity (String studiedLine){
		// сразу делаем индентификатор для строки
		int len = studiedLine.length();
		if (maxOneLineLong <= len){
			int idLen = len/2;
			lastIdentity = studiedLine.substring(idLen, len);
		}
	}
	//и то как им пользоваться - проверять входную строку
	private boolean compareIdentity(String studiedLine){
		// при чем эта проверка нужна только для отделения малой строки от отделенного куска
		// возвращает правду если равны
		Integer
			idLen = lastIdentity.length(),
			len = studiedLine.length(),
			counter = null;
		char[]
			idCh = lastIdentity.toCharArray(),
			ch = studiedLine.toCharArray();
		boolean test = false;
		if (idLen == len) test = lastIdentity.equals(studiedLine) ? true : false ;
		else if (len < idLen){
			counter = len-1;
		}else if (idLen < len){
			counter = idLen -1;
		}
		while (counter != null && counter>=0){
			if (ch[counter] != idCh[counter]) test = false;
			counter--;
		}
		if (!test) sentAmount = 0; // т.к предложение не является продолжением предыдущего
		return test;
	}
	private boolean lineTest (String studiedLine) {
		// тест всего предложения
		if (countSzСriterion(studiedLine)){ // нужно добавить реакцию если одно предложение разделялось
			return true;
		}
		return false;
	}
	private boolean pointTest (String studiedLine, int sepPoint) {
		// метод проверки точки предложения по всем критериям
		// исключительно читает поля, не изменяет их.
		char[] chOfLine = studiedLine.toCharArray();
		int shift = sepPoint > 0 ? 1 : 0;

		// нужна поправка на размер предложения. Плюс нужно ловить изменения в одном предложении.
		// чтобы не перепутать маленкую строку с отделенным куском от предложения
		// у отделенного куска предыдущего предложения есть характерные черты (последние несколько символов)
		// т.е сначала проверяется размер, а после последние 3 символа.
		 // TODO пока тестовый критерий - проверить на практике

		// запуск всех проверок при инициализации переменной.
		// TODO Новые проверки добавлять в эту переменную
		// Для проверки важен порядок следования.
		// PTLSС проверяется первым, т.к BCC и NLCC зависят от расположения точки, которое должно быть дальше 1 символа
		boolean checkAll = true;

			/*countPTLSСriterion(sepPoint) &&
			countBCСriterion(chOfLine[separatorPoint-shift]) &&
			countNLCСriterion(chOfLine[separatorPoint-shift]) &&
			countMxSAСriterion() &&
			countPTSCriterion(chOfLine, separatorPoint) &&
			countPTPCriterion () &&
			countPTLSPCriterion ();*/

		// альтернатива 2 для работы метода displayParam
		params[0] = countPTLSСriterion(sepPoint);
		params[1] = countBCСriterion(chOfLine[sepPoint-shift]);
		params[2] = countNLCСriterion(chOfLine[sepPoint-shift]);
		params[3] = countMxSAСriterion();
		params[4] = countPTSCriterion(chOfLine, sepPoint);
		params[5] = countPTPCriterion ();
		params[6] = countPTLSPCriterion ();

		for (boolean res : params){
			checkAll = checkAll && res;
		}

		// если ни один из параметров не true, то проверка пройдена и точка означает точку предложения.
		return checkAll;
	}

	private enum Bechavior {SentStart,SentPoint, NsentPoint, Another}
	private void changesInValsForBechavior (Bechavior bech) {
		// значительно изменяет состояние внутренних перемнных
		switch (bech) {
			case SentStart:
				separatorPoint = 0; //однострочный параметр
				lineLen = 0;
				for (boolean res : params){
					res = false;
				}
				result = false; // пока я не вызвал новую проверку результат предыдущей доступен
				break;
			case SentPoint:
				//Обнуление всех изменяемых переменных
				pointToPointGap = 0; // межстрочный параметра
				pointToLstSentPointGap = 0;
				sentAmount++;
				//separatesNumber++; // тригерит изменение - количество разделителей в одном предложении
				result = false; //для внешней среды значит в предложении нет точек
				params = new boolean[]{false,false,false,false,false,false,false};
				break;

			case NsentPoint:
				// если точка не является концом предложения
				// обнуляю межточечный параметр
				// увеличиваю параметр расстояния между точками предложения
				pointToPointGap = 0;
				pointToLstSentPointGap++;
				break;

			case Another:
				// если любой другой символ
				//увеличиваю межточечный параметр, расстояния между точками предложения
				pointToPointGap++;
				pointToLstSentPointGap++;
				break;
		}
	}

	public boolean sentenseTest (String studiedLine){
		//основной метод проверки предложения. Для текста создается один раз, между текстами пересоздается.
		// в метод поступает полная строка из текста
		// происходит проход по всем знакам, поиск точек, сбор всей доступной информации (заполнение критериев).
		// для точек производится отдельная проверка внутренним методом
		// возвращает результат о наличии точки в предложении.
		// Если точка имеется становится возможным запросить первую подходящую из этого предложения.
		// Разбитие предложения производится во внешней среде
		changesInValsForBechavior(Bechavior.SentStart); // подготовка парамеров проверки.

		char[] ch = studiedLine.toCharArray();
		lineLen = ch.length; // Для возвращения при отсутствии точки предложения. Так закончится внешний цикл.
		int counter = 0;

		if (lineTest(studiedLine)) return false;
		// цикл проходит по строке символов
		while (counter != lineLen){
			// проверка текущего символа
			if (ch[counter] == '.'){// если находит точку

				if (pointTest (studiedLine, counter) ) { // если точка предложения
					// Значит найденно предложение.
					// Нужно отправить частсть предложения до точки
					// в переменную для предложений класса данных фрагмента.
					// При этом обнуляюся межстрочных значения определения точки (характерные критерии).

					changesInValsForBechavior(Bechavior.SentPoint);// Обнуляет все исследуемые параметры
					result = true; //для внешней среды значит есть искомая точка
					separatorPoint = counter;
					break;  //выход из данного цикла

				} else changesInValsForBechavior(Bechavior.NsentPoint); // точка не предложение
			} else changesInValsForBechavior(Bechavior.Another); // любой другой символ

			counter++;
		}
		setLastIdentity(studiedLine);
		return result;
	}

}