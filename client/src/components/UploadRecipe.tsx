import React, { useState } from "react";
import { CheckCircle, XCircle, Loader2 } from "lucide-react";
import { useAuth } from "../hooks/useAuth";

const UploadRecipe: React.FC = () => {
  const [status, setStatus] = useState<
    "idle" | "uploading" | "success" | "already_uploaded" | "error"
  >("idle");

  const { user } = useAuth();
  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setStatus("uploading");

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("/api/genai/upload", {
        method: "POST",
        body: formData,
        headers: {
          Authorization: `Bearer ${user?.token}`,
        },
      });

      if (!response.ok) throw new Error("Upload failed");
      
      const data = await response.json();
      const message = data.message || "";
      
      if (message.includes("already uploaded")) {
        setStatus("already_uploaded");
      } else {
        setStatus("success");
      }

      setTimeout(() => setStatus("idle"), 3000);
    } catch (err) {
      console.error(err);
      setStatus("error");
      setTimeout(() => setStatus("idle"), 3000);
    }
  };

  return (
    <div className="p-4 border-t border-gray-200 dark:border-gray-700">
      {status === "idle" && (
        <form>
          <label className="flex flex-col items-center justify-center px-4 py-2 text-sm font-medium text-white bg-green-600 rounded-md cursor-pointer hover:bg-green-700 transition-colors">
            Upload Recipe Book
            <input type="file" accept=".pdf" hidden onChange={handleUpload} />
          </label>
        </form>
      )}

      {status === "uploading" && (
        <div className="flex items-center justify-center gap-2 text-sm text-blue-600">
          <Loader2 className="animate-spin w-5 h-5" />
          Uploading...
        </div>
      )}

      {status === "success" && (
        <div className="flex items-center justify-center gap-2 text-sm text-green-600">
          <CheckCircle className="w-5 h-5" />
          Uploaded successfully!
        </div>
      )}

      {status === "already_uploaded" && (
        <div className="flex items-center justify-center gap-2 text-sm text-yellow-600">
          <CheckCircle className="w-5 h-5" />
          File Already Uploaded!
        </div>
      )}

      {status === "error" && (
        <div className="flex items-center justify-center gap-2 text-sm text-red-600">
          <XCircle className="w-5 h-5" />
          Upload failed.
        </div>
      )}
    </div>
  );
};

export default UploadRecipe;
