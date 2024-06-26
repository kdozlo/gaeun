import { useEffect, useState } from "react";
import FavoriteList from "../../components/favorite/FavoriteList";
import FavoriteGetForm from "../../services/favorites/FavoriteGetService";
import { FavoriteResponse, FavoriteState } from "../../types/FavoriteType";

import logo from "../../../public/windows11/LargeTile.scale-100.png";

const Favorite = () => {
  const [favoriteState, setFavoriteState] = useState<FavoriteState>({
    favorites: [],
    totalCnt: 0,
    page: 0,
    loading: false,
    hasNext: false,
    scrollPosition: 0,
  });

  useEffect(() => {
    const fetchFavorites = async () => {
      setFavoriteState((prevState) => ({ ...prevState, loading: true }));
      try {
        const response: FavoriteResponse = await FavoriteGetForm({
          page: favoriteState.page,
          size: 20,
        });
        setFavoriteState((prevState) => ({
          ...prevState,
          favorites: [
            ...prevState.favorites,
            ...(response.data.favorites || []),
          ],
          totalCnt: response.data.totalCnt,
          hasNext: response.data.hasNext,
        }));
      } catch (error) {
        console.error("에러발생 에러발생! ", error);
      } finally {
        setFavoriteState((prevState) => ({ ...prevState, loading: false }));
      }
    };

    fetchFavorites();
  }, [favoriteState.page]);

  useEffect(() => {
    const handleScroll = () => {
      const scrollHeight = document.documentElement.scrollHeight;
      const scrollTop = document.documentElement.scrollTop;
      const clientHeight = document.documentElement.clientHeight;

      if (
        scrollTop + clientHeight >= scrollHeight &&
        !favoriteState.loading &&
        favoriteState.hasNext
      ) {
        setFavoriteState((prevState) => ({
          ...prevState,
          page: prevState.page + 1,
        }));
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [favoriteState.loading, favoriteState.hasNext]);

  return (
    <div className="pt-12 ">
      <div className="fixed flex items-center w-full bg-gray-100 border-2 border-gray-200">
        <p className="p-2 pt-4 text-sm font-bold">내가 찜한 맛집</p>
        <p className="pt-2 text-xs font-bold text-gray-500">
          {favoriteState.totalCnt}개
        </p>
      </div>
      <div className="pt-12 pb-14">
        {/* 찜 리스트 */}
        {favoriteState.favorites.length > 0 ? (
          <FavoriteList favorites={favoriteState.favorites} />
        ) : (
          <div className="h-screen pb-40 center">
            <div>
              <img className="rounded-full" src={logo} alt="로고" />
              <h2 className="text-lg font-bold center">
                나만의 천사 가게를
                <span
                  className="mx-2 text-4xl"
                  style={{ fontFamily: "MyFont" }}
                >
                  찜
                </span>
                해보세요
              </h2>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Favorite;
