import { useEffect, useState } from "react";
import { useUser } from "../contexts/userContext";
import { useUserPreferencesUpdate } from "../hooks/useUserPreferencesUpdate";
import { DIETARY_PREFERENCES } from "../types/enums";

const defaultPreferences = {
  vegetarian: false,
  vegan: false,
  glutenFree: false,
  dairyFree: false,
  nutFree: false,
  spicyFood: false,
};

const getPreferenceMap = (list: DietaryPreference[]) => ({
  vegetarian: list.includes(DIETARY_PREFERENCES.VEGETARIAN),
  vegan: list.includes(DIETARY_PREFERENCES.VEGAN),
  glutenFree: list.includes(DIETARY_PREFERENCES.GLUTEN_FREE),
  dairyFree: list.includes(DIETARY_PREFERENCES.DAIRY_FREE),
  nutFree: list.includes(DIETARY_PREFERENCES.NUT_FREE),
  spicyFood: list.includes(DIETARY_PREFERENCES.SPICY_FOOD),
});

const getPreferenceList = (preferences: typeof defaultPreferences) => {
  const list: DietaryPreference[] = [];
  if (preferences.vegetarian) list.push(DIETARY_PREFERENCES.VEGETARIAN);
  if (preferences.vegan) list.push(DIETARY_PREFERENCES.VEGAN);
  if (preferences.glutenFree) list.push(DIETARY_PREFERENCES.GLUTEN_FREE);
  if (preferences.dairyFree) list.push(DIETARY_PREFERENCES.DAIRY_FREE);
  if (preferences.nutFree) list.push(DIETARY_PREFERENCES.NUT_FREE);
  if (preferences.spicyFood) list.push(DIETARY_PREFERENCES.SPICY_FOOD);
  return list;
};

const PreferencesPage: React.FC = () => {
  const { user } = useUser();
  const { updateUserPreferences } = useUserPreferencesUpdate({});
  const [preferences, setPreferences] = useState(defaultPreferences);

  useEffect(() => {
    setPreferences(
      user?.dietaryPreferences
        ? getPreferenceMap(user.dietaryPreferences)
        : defaultPreferences
    );
  }, [user?.dietaryPreferences]);

  const handleToggle = (key: keyof typeof preferences) => {
    const updatedPreferences = { ...preferences, [key]: !preferences[key] };
    setPreferences(updatedPreferences);
    const updatedPreferenceList = getPreferenceList(updatedPreferences);
    updateUserPreferences({ preferences: updatedPreferenceList });
  };

  return (
    <div className="flex-1 overflow-y-auto p-6">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">Dietary Preferences</h1>

        <div className="space-y-6">
          <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold mb-4">Dietary Restrictions</h2>

            <div className="space-y-4">
              {Object.entries(preferences).map(([key, value]) => (
                <div key={key} className="flex items-center justify-between">
                  <div>
                    <h3 className="font-medium capitalize">
                      {key.replace(/([A-Z])/g, " $1").trim()}
                    </h3>
                    <p className="text-sm text-gray-500 dark:text-gray-400">
                      {`Exclude ${key
                        .replace(/([A-Z])/g, " $1")
                        .toLowerCase()
                        .trim()} items`}
                    </p>
                  </div>
                  <button
                    onClick={() =>
                      handleToggle(key as keyof typeof preferences)
                    }
                    className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                      value ? "bg-blue-600" : "bg-gray-200 dark:bg-gray-700"
                    }`}
                  >
                    <span
                      className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform ${
                        value ? "translate-x-6" : "translate-x-1"
                      }`}
                    />
                  </button>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6">
            <p className="text-sm text-gray-500 dark:text-gray-400">
              These preferences will be used to customize your recipe
              recommendations and filter out incompatible ingredients.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PreferencesPage;
