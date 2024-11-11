import { Bell } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

interface Notice {
  id: number;
  title: string;
  date: string;
}

interface NoticeCardProps {
  notices: Notice[];
}

export const NoticeCard = ({ notices }: NoticeCardProps) => {
  return (
    <Card className="h-fit">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Bell className="h-5 w-5 text-blue-600" />
          Latest Notices
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {notices.map((notice) => (
            <div
              key={notice.id}
              className="p-4 border rounded-lg hover:bg-gray-50"
            >
              <h3 className="font-medium">{notice.title}</h3>
              <p className="text-sm text-gray-500">{notice.date}</p>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
};
