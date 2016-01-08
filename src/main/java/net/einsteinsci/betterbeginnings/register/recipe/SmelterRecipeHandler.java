package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.Map.Entry;

public class SmelterRecipeHandler
{
	private static final SmelterRecipeHandler SMELTINGBASE = new SmelterRecipeHandler();

	private Map<ItemStack, Float> experienceList = new HashMap<>();

	private List<SmelterRecipe> recipes = new ArrayList<>();

	private SmelterRecipeHandler()
	{
		// nothing here
	}

	public static void addRecipe(Item input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		smelting().putLists(input, output, experience, gravel, bonus, chance);
	}

	public void putLists(Item input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		putLists(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), output, experience, gravel, bonus, chance);
	}

	public static SmelterRecipeHandler smelting()
	{
		return SMELTINGBASE;
	}

	public void putLists(ItemStack input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		experienceList.put(output, experience);

		recipes.add(new SmelterRecipe(output, input, experience, gravel, bonus, chance));
	}

	public static void addRecipe(String input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		for (ItemStack stack : OreDictionary.getOres(input))
		{
			smelting().putLists(stack, output, experience, gravel, bonus, chance);
		}
	}
	
	public static void addRecipe(Block input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		smelting().putLists(Item.getItemFromBlock(input), output, experience, gravel, bonus, chance);
	}

	public static void addRecipe(ItemStack input, ItemStack output, float experience, int gravel, int bonus, float chance)
	{
		smelting().putLists(input, output, experience, gravel, bonus, chance);
	}

	public ItemStack getSmeltingResult(ItemStack input)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput().getItem() == input.getItem())
			{
				return recipe.getOutput();
			}
		}

		return null;
	}

	public int getGravelCount(ItemStack stack)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput() != null)
			{
				if (recipe.getInput().getItem() == stack.getItem())
				{
					return recipe.getGravel();
				}
			}
		}

		return -1;
	}

	public float giveExperience(ItemStack stack)
	{
		Iterator iterator = experienceList.entrySet().iterator();
		Entry entry;

		do
		{
			if (!iterator.hasNext())
			{
				return 0.0f;
			}

			entry = (Entry)iterator.next();
		} while (!canBeSmelted(stack, (ItemStack)entry.getKey()));

		if (stack.getItem().getSmeltingExperience(stack) != -1)
		{
			return stack.getItem().getSmeltingExperience(stack);
		}

		return (Float)entry.getValue();
	}

	private boolean canBeSmelted(ItemStack stack, ItemStack stack2)
	{
		return stack2.getItem() == stack.getItem() &&
				(stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == stack
						.getItemDamage());
	}

	public int getBonus(ItemStack input)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput().getItem() == input.getItem())
			{
				return recipe.getBonus();
			}
		}

		return 0;
	}

	public float getBonusChance(ItemStack input)
	{
		for (SmelterRecipe recipe : recipes)
		{
			if (recipe.getInput().getItem() == input.getItem())
			{
				return recipe.getBonusChance();
			}
		}

		return 0.0f;
	}

	public static List<SmelterRecipe> getRecipes()
	{
		return smelting().recipes;
	}
}
