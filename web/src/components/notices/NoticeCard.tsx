import { useEffect, useState } from "react";
import { Bell } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

interface Notice {
  id: string; // UUID from the API
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
        const response = await fetch("http://localhost:8080/api/notices");
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

  if (loading) return <div>Loading notices...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2 className="text-lg font-bold mb-4 flex items-center">
        <Bell className="mr-2" /> Latest Notices
      </h2>
      <div className="space-y-4">
        {notices.map((notice) => (
          <Card key={notice.id} onClick={() => setSelectedNotice(notice)} className="cursor-pointer">
            <CardHeader>
              <CardTitle>{notice.title}</CardTitle>
            </CardHeader>
            <CardContent>
              <p>{notice.date}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Dialog for displaying notice details */}
      <Dialog open={!!selectedNotice} onOpenChange={() => setSelectedNotice(null)}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{selectedNotice?.title}</DialogTitle>
            <DialogDescription>{selectedNotice?.date}</DialogDescription>
          </DialogHeader>
          <p>{selectedNotice?.notice}</p>
          <Button className="mt-4" onClick={() => setSelectedNotice(null)}>
            Close
          </Button>
        </DialogContent>
      </Dialog>
    </div>
  );
};
