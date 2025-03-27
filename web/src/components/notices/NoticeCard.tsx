import { useEffect, useState } from "react";
import { Bell } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

interface Notice {
  id: string;
  title: string;
  date: string;
  notice: string;
}

export const NoticeCard = () => {
  const [notices, setNotices] = useState<Notice[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedNotice, setSelectedNotice] = useState<Notice | null>(null);

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        const response = await fetch("/api/v1/notice");
        if (!response.ok) {
          throw new Error(`Error: ${response.statusText}`);
        }
        const data: Notice[] = await response.json();
        setNotices(data);
      } catch (err) {
        setError("Failed to load notices.");
      } finally {
        setLoading(false);
      }
    };
    fetchNotices();
  }, []);

  return (
    <div className="w-full max-w-md border rounded-lg bg-card">
      <div className="p-6">
        <h2 className="text-xl font-semibold mb-4 flex items-center gap-2 text-card-foreground">
          <Bell className="h-6 w-6 text-primary" />
          Latest Notices
        </h2>
        <div className="space-y-4">
          {loading && (
            <p className="text-muted-foreground">Loading notices...</p>
          )}
          {error && <p className="text-destructive">{error}</p>}
          {!loading &&
            !error &&
            notices.map((notice) => (
              <div
                key={notice.id}
                className="p-4 border rounded-md cursor-pointer hover:bg-accent transition"
                onClick={() => setSelectedNotice(notice)}
              >
                <h3 className="text-base font-semibold text-card-foreground">
                  {notice.title}
                </h3>
                <p className="text-sm text-muted-foreground">{notice.date}</p>
              </div>
            ))}
        </div>
      </div>

      <Dialog
        open={!!selectedNotice}
        onOpenChange={() => setSelectedNotice(null)}
      >
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{selectedNotice?.title}</DialogTitle>
            <DialogDescription>{selectedNotice?.date}</DialogDescription>
          </DialogHeader>
          <p className="text-sm">{selectedNotice?.notice}</p>
          <Button onClick={() => setSelectedNotice(null)} className="mt-4">
            Close
          </Button>
        </DialogContent>
      </Dialog>
    </div>
  );
};
