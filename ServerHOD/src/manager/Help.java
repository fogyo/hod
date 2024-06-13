package manager;

public class Help extends Command{
	
	private String str = "";
	
	@Override 
	public void run() {
		str = "save_receipt requires receipt_id: Сохраняет рецепт с выбранным id \n"
				+ "delete_receipt requires receipt_id: Удаляет рецепт с выбранным id из сохраненных \n"
				+ "sort_receipts_by_time: Выводит рецепты по времени их приготовления от меньшего к большему \n"
				+ "forbid_ingredient requires ingredient_id: Запрещает ингредиент с выбранным id \n"
				+ "permit_ingredient requires ingredient_id: Удаляет ингредиент с выбранным id из запрещенных \n"
				+ "show_saved_receipts: Показывает сохраненные рецепты пользователя \n"
				+ "help: Выводит доступные команды \n"
				+ "exit: Завершает программу \n"
				+ "show_receipts: Выводит все рецеты \n"
				+ "show_ingredients: Выводит все ингредиенты \n"
				+ "show_receipt requires receipt_id: Выводит информацию о рецепте \n"
				+ "show_permitted_receipts: Выводит доступные рецепты \n"
				+ "show_permitted_ingredients: Выводит доступные ингредиенты";
	}
	
	public String getString() {
		return str;
	}

}
