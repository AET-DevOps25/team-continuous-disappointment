export const getUserInfo = async (token: string): Promise<User> => {
  try {
    const response = await fetch("/api/user/info", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status} ${response.statusText}`);
    }

    const userData = await response.json();
    return { ...userData, token };
  } catch (error) {
    console.error("Failed to fetch user data:", error);
    throw error;
  }
};

export const updateUserPreferences = async (
  token: string,
  preferences: DietaryPreference[]
): Promise<void> => {
  try {
    const response = await fetch("/api/user/preferences", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ dietaryPreferences: preferences }),
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status} ${response.statusText}`);
    }
  } catch (error) {
    console.error("Failed to update user preferences:", error);
    throw error;
  }
};
