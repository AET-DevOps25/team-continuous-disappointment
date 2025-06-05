export async function getConversationsOfUser(
  token: string
): Promise<Conversation[]> {
  if (!token) {
    throw new Error("User ID and token are required to fetch conversations.");
  }
  try {
    const conversationsResponse = await fetch("/api/chat", {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!conversationsResponse.ok) {
      throw new Error("Failed to fetch conversations.");
    }
    const conversations = await conversationsResponse.json();
    console.log("Fetched conversations:", conversations);
    return conversations as Conversation[];
  } catch (error) {
    console.error("Error fetching conversations:", error);
    throw new Error("Failed to fetch conversations.");
  }
}

export async function addMessageToConversation(
  token: string,
  conversationId: string,
  message: string
): Promise<Message> {
  if (!conversationId || !message || !token) {
    throw new Error("Conversation ID, message, and token are required.");
  }
  try {
    const response = await fetch(`/api/chat/${conversationId}/messages`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ content: message }),
    });
    if (!response.ok) {
      throw new Error("Failed to add message to conversation.");
    }
    return await response.json();
  } catch (error) {
    console.error("Error adding message:", error);
    throw new Error("Failed to add message to conversation.");
  }
}

export async function createConversation(
  token: string,
  title: string
): Promise<Conversation> {
  if (!token || !title) {
    throw new Error(
      "User ID, token, and title are required to create a conversation."
    );
  }
  try {
    const response = await fetch(`/api/chat`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ title }),
    });
    if (!response.ok) {
      throw new Error("Failed to create conversation.");
    }
    return await response.json();
  } catch (error) {
    console.error("Error creating conversation:", error);
    throw new Error("Failed to create conversation.");
  }
}

export async function deleteConversation(
  token: string,
  conversationId: string
): Promise<void> {
  if (!token || !conversationId) {
    throw new Error(
      "User ID, token, and conversation ID are required to delete a conversation."
    );
  }
  try {
    const response = await fetch(`/api/chat/${conversationId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Failed to delete conversation.");
    }
  } catch (error) {
    console.error("Error deleting conversation:", error);
    throw new Error("Failed to delete conversation.");
  }
}
