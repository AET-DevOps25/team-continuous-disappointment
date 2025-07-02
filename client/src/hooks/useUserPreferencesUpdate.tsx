import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useUser } from "../contexts/userContext";
import { updateUserPreferences } from "../services/userService";

interface UseUserPreferencesUpdate {
  onSuccess?: () => void;
  onError?: (error: Error) => void;
}

export const useUserPreferencesUpdate = ({
  onSuccess,
  onError,
}: UseUserPreferencesUpdate) => {
  const { user } = useUser();
  const queryClient = useQueryClient();
  const { mutate, isError, error } = useMutation({
    mutationKey: ["userPreferenceUpdate", user?.id],
    mutationFn: ({ preferences }: { preferences: DietaryPreference[] }) =>
      updateUserPreferences(user!.token, preferences),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userInfo"] });
      if (onSuccess) {
        onSuccess();
      }
    },
    onError: (error: Error) => {
      console.error("Error updating user preferences:", error);
      if (onError) {
        onError(error);
      }
    },
  });

  return { updateUserPreferences: mutate, isError, error };
};
